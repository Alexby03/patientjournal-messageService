package core.services;

import api.dto.SessionDTO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class SessionService {

    @Inject
    SessionRepository sessionRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    MessageRepository messageRepository;


    public List<SessionDTO> getUserSessions(UUID userId, boolean eagerMessages) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Session> sessions = sessionRepository.findAllUserSessions(userId);
        return sessions.stream()
                .map(s -> DTOMapper.toSessionDTO(s, eagerMessages))
                .collect(Collectors.toList());
    }

    public SessionDTO getSessionById(UUID sessionId, boolean includeMessages) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        if (includeMessages) {
            List<Message> messages = messageRepository.findBySessionId(sessionId); // Assuming returns List<Message>
            session.setMessages(messages);
        }

        return DTOMapper.toSessionDTO(session, includeMessages);
    }

    public List<SessionDTO> getSessionsBetweenUsers(UUID userId1, UUID userId2, boolean eagerMessages) {
        User user1 = userRepository.findById(userId1);
        if (user1 == null) {
            throw new IllegalArgumentException("User 1 not found");
        }

        User user2 = userRepository.findById(userId2);
        if (user2 == null) {
            throw new IllegalArgumentException("User 2 not found");
        }

        List<Session> sessions = sessionRepository.findSessionsBetweenUsers(userId1, userId2);
        return sessions.stream()
                .map(s -> DTOMapper.toSessionDTO(s, eagerMessages))
                .collect(Collectors.toList());
    }

    public List<SessionDTO> searchSessionsBySubject(String searchTerm, boolean eagerMessages) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        List<Session> sessions = sessionRepository.searchBySubject(searchTerm);
        return sessions.stream()
                .map(s -> DTOMapper.toSessionDTO(s, eagerMessages))
                .collect(Collectors.toList());
    }

    @Transactional
    public SessionDTO createSession(SessionDTO dto) {
        validateCreateDTO(dto);

        User sender = userRepository.findById(dto.senderId);
        if (sender == null) {
            throw new IllegalArgumentException("Sender not found");
        }

        User receiver = userRepository.findById(dto.receiverId);
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found");
        }

        Session session = new Session(
                sender.getId(),
                receiver.getId(),
                dto.subject,
                LocalDateTime.now()
        );

        sessionRepository.persist(session);
        return DTOMapper.toSessionDTO(session, false);
    }

    @Transactional
    public boolean deleteSession(UUID sessionId) {
        return sessionRepository.deleteById(sessionId);
    }

    public long countUserSessions(UUID userId) {
        return sessionRepository.countUserSessions(userId);
    }

    private void validateCreateDTO(SessionDTO dto) {
        if (dto.senderId == null) {
            throw new IllegalArgumentException("Sender ID is required");
        }
        if (dto.receiverId == null) {
            throw new IllegalArgumentException("Receiver ID is required");
        }
        if (dto.subject == null || dto.subject.isEmpty()) {
            throw new IllegalArgumentException("Subject is required");
        }
    }
}
