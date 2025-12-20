package se.kth.patientjournal;

import core.enums.UserType;
import data.entities.Message;
import data.entities.Session;
import data.entities.User;
import data.repositories.MessageRepository;
import data.repositories.SessionRepository;
import data.repositories.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MessageRepositoryTest {

    @Inject
    MessageRepository messageRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    UserRepository userRepository;

    User sender;
    Session session;
    Message message1;
    Message message2;

    @Test
    @TestTransaction
    void cleanUp() {
        messageRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        sender = new User("Test Sender", "test@example.com", "password", UserType.Doctor);
        userRepository.persist(sender);

        session = new Session(sender.getId(), UUID.randomUUID(), "Test Subject", LocalDateTime.now());
        sessionRepository.persist(session);

        message1 = new Message(session, sender, "Hello world");
        message1.setDateTime(LocalDateTime.now());
        messageRepository.persist(message1);

        message2 = new Message(session, sender, "Another message");
        message2.setDateTime(LocalDateTime.now().plusMinutes(3));
        messageRepository.persist(message2);
    }

    @Test
    @TestTransaction
    void userPersistence_withAllUserTypes() {
        cleanUp();
        for (UserType type : UserType.values()) {
            User user = new User("Test " + type, type.name().toLowerCase() + "@example.com", "password", type);
            userRepository.persist(user);

            User fetched = userRepository.findById(user.getId());
            assertNotNull(fetched, "Fetched user should not be null");
            assertEquals(type, fetched.getUserType(), "Enum value should match for " + type);
        }
    }

    @Test
    @TestTransaction
    void findBySessionId_returnsAllMessages() {
        cleanUp();
        List<Message> messages = messageRepository.findBySessionId(session.getSessionId());
        assertEquals(2, messages.size());
        assertTrue(messages.contains(message1));
        assertTrue(messages.contains(message2));
    }

    @Test
    @TestTransaction
    void findLatestMessageInSession_returnsNewestMessage() {
        cleanUp();
        Message latest = messageRepository.findLatestMessageInSession(session.getSessionId());
        assertEquals(message2.getMessage(), latest.getMessage());
    }

    @Test
    @TestTransaction
    void searchByMessageContent_findsMatchingMessages() {
        cleanUp();
        List<Message> messages = messageRepository.searchByMessageContent("Hello");
        assertEquals(1, messages.size());
        assertEquals(message1.getMessage(), messages.get(0).getMessage());
    }

    @Test
    @TestTransaction
    void countBySession_returnsCorrectCount() {
        cleanUp();
        Long count = messageRepository.countBySession(session.getSessionId());
        assertEquals(2L, count);
    }

    @Test
    @TestTransaction
    void findBySessionIdWithRelations_returnsAllMessages() {
        cleanUp();
        List<Message> messages = messageRepository.findBySessionIdWithRelations(session.getSessionId());
        assertEquals(2, messages.size());
        assertEquals(sender.getId(), messages.get(0).getSender().getId());
    }

    @Test
    @TestTransaction
    void findSessionMessagesWithPagination_returnsPagedMessages() {
        cleanUp();
        List<Message> page = messageRepository.findSessionMessagesWithPagination(session.getSessionId(), 0, 1);
        assertEquals(1, page.size());
    }
}
