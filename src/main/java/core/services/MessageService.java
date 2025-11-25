package core.services;

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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    UserRepository userRepository;

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
        if (dto.sessionId == null) {
            throw new IllegalArgumentException("Session ID is required");
        }
        if (dto.senderId == null) {
            throw new IllegalArgumentException("Sender ID is required");
        }
        if (dto.message == null || dto.message.isEmpty()) {
            throw new IllegalArgumentException("Message content is required");
        }

        Session session = sessionRepository.findById(dto.sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        User sender = userRepository.findById(dto.senderId);
        if (sender == null) {
            throw new IllegalArgumentException("Sender not found");
        }

        Message message = new Message(session, sender, dto.message);
        messageRepository.persist(message);

        return DTOMapper.toMessageDTO(message);
    }

    @Transactional
    public boolean deleteMessage(UUID messageId) {
        return messageRepository.deleteById(messageId);
    }
}
