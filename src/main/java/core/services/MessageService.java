package core.services;

import api.dto.MessageCreatedEvent;
import api.dto.MessageDTO;
import core.mappers.DTOMapper;
import data.entities.Message;
import data.entities.Session;
import data.entities.User;
import data.repositories.MessageRepository;
import data.repositories.SessionRepository;
import data.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MessageService {

    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final Emitter<MessageCreatedEvent> eventEmitter;

    @Inject
    public MessageService(MessageRepository messageRepository,
                          SessionRepository sessionRepository,
                          UserRepository userRepository,
                          @Channel("message-events-out") Emitter<MessageCreatedEvent> eventEmitter) {

        this.messageRepository = messageRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.eventEmitter = eventEmitter;
    }

    // Tom konstruktor för tester (valfritt)
    public MessageService() {
        this.messageRepository = null;
        this.sessionRepository = null;
        this.userRepository = null;
        this.eventEmitter = null;
    }

    public List<MessageDTO> getSessionMessages(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }
        List<Message> messages = messageRepository.findBySessionId(sessionId);
        return messages.stream()
                .map(DTOMapper::toMessageDTO)
                .collect(Collectors.toList());
    }

    public MessageDTO getMessageById(UUID messageId) {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new IllegalArgumentException("Message not found");
        }
        return DTOMapper.toMessageDTO(message);
    }

    public MessageDTO getLatestMessage(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }
        Message latest = messageRepository.findLatestMessageInSession(sessionId);
        return DTOMapper.toMessageDTO(latest);
    }

    public List<MessageDTO> searchMessages(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        List<Message> messages = messageRepository.searchByMessageContent(searchTerm);
        return messages.stream()
                .map(DTOMapper::toMessageDTO)
                .collect(Collectors.toList());
    }

    public long countSessionMessages(UUID sessionId) {
        return messageRepository.countBySession(sessionId);
    }

    @Transactional
    public MessageDTO createMessage(MessageDTO dto) {
        System.out.println("DEBUG: createMessage called with DTO: " + dto);

        if (dto.sessionId == null) {
            System.err.println("ERROR: Session ID is missing");
            throw new IllegalArgumentException("Session ID is required");
        }
        if (dto.senderId == null) {
            System.err.println("ERROR: Sender ID is missing");
            throw new IllegalArgumentException("Sender ID is required");
        }
        if (dto.message == null || dto.message.isEmpty()) {
            System.err.println("ERROR: Message content is empty");
            throw new IllegalArgumentException("Message content is required");
        }

        Session session = sessionRepository.findById(dto.sessionId);
        if (session == null) {
            System.err.println("ERROR: Session not found for ID: " + dto.sessionId);
            throw new IllegalArgumentException("Session not found");
        }

        User sender = userRepository.findById(dto.senderId);
        if (sender == null) {
            System.err.println("ERROR: Sender not found for ID: " + dto.senderId);
            throw new IllegalArgumentException("Sender not found");
        }

        Message message = new Message(session, sender, dto.message);
        messageRepository.persist(message);
        System.out.println("DEBUG: Message persisted to DB with ID: " + message.getMessageId());

        MessageCreatedEvent event = new MessageCreatedEvent();
        event.messageId = message.getMessageId();
        event.sessionId = message.getSessionId();
        event.senderId = sender.getId();

        if (session.getSenderId().equals(sender.getId())) {
            event.receiverId = session.getReceiverId();
        } else {
            event.receiverId = session.getSenderId();
        }

        event.content = message.getMessage();
        event.timestamp = System.currentTimeMillis();

        System.out.println("DEBUG: Prepared Kafka event for Receiver: " + event.receiverId);

        try {
            CompletionStage<Void> future = eventEmitter.send(event);

            future.whenComplete((success, failure) -> {
                if (failure != null) {
                    System.err.println("ERROR: Failed to send to Kafka: " + failure.getMessage());
                    failure.printStackTrace();
                } else {
                    System.out.println("DEBUG: Successfully sent to Kafka topic!");
                }
            });

        } catch (Exception e) {
            System.err.println("ERROR: Exception during Kafka send call: " + e.getMessage());
            e.printStackTrace();
        }

        return DTOMapper.toMessageDTO(message);
    }

    @Transactional
    public boolean deleteMessage(UUID messageId) {
        return messageRepository.deleteById(messageId);
    }
}