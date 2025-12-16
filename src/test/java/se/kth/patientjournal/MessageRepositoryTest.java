//package se.kth.patientjournal;
//
//import core.enums.UserType;
//import data.entities.Message;
//import data.entities.Session;
//import data.entities.User;
//import data.repositories.MessageRepository;
//import data.repositories.SessionRepository;
//import data.repositories.UserRepository;
//import io.quarkus.test.junit.QuarkusTest;
//import jakarta.inject.Inject;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@QuarkusTest
//class MessageRepositoryTest {
//
//    @Inject
//    MessageRepository messageRepository;
//
//    @Inject
//    SessionRepository sessionRepository;
//
//    @Inject
//    UserRepository userRepository;
//
//    User sender;
//    Session session;
//    Message message1;
//    Message message2;
//
//    @BeforeEach
//    @Transactional
//    void setUp() {
//        // Rensa databasen innan varje test
//        messageRepository.deleteAll();
//        sessionRepository.deleteAll();
//        userRepository.deleteAll();
//
//        // Skapa testanvändare
//        sender = new User("Test Sender", "test@example.com", "password", UserType.Doctor);
//        userRepository.persist(sender);
//
//        // Skapa testsession
//        session = new Session(sender.getId(), UUID.randomUUID(), "Test Subject", LocalDateTime.now());
//        sessionRepository.persist(session);
//
//        // Skapa testmeddelanden
//        message1 = new Message(session, sender, "Hello world");
//        messageRepository.persist(message1);
//
//        message2 = new Message(session, sender, "Another message");
//        messageRepository.persist(message2);
//    }
//
//    @Test
//    void userPersistence_withAllUserTypes() {
//        for (UserType type : UserType.values()) {
//            User user = new User("Test " + type, type.name().toLowerCase() + "@example.com", "password", type);
//            userRepository.persist(user);
//
//            User fetched = userRepository.findById(user.getId());
//            assertNotNull(fetched, "Fetched user should not be null");
//            assertEquals(type, fetched.getUserType(), "Enum value should match for " + type);
//        }
//    }
//
//    @Test
//    void findBySessionId_returnsAllMessages() {
//        List<Message> messages = messageRepository.findBySessionId(session.getSessionId());
//        assertEquals(2, messages.size());
//        assertTrue(messages.contains(message1));
//        assertTrue(messages.contains(message2));
//    }
//
//    @Test
//    void findLatestMessageInSession_returnsNewestMessage() {
//        Message latest = messageRepository.findLatestMessageInSession(session.getSessionId());
//        assertEquals(message2.getMessage(), latest.getMessage());
//    }
//
//    @Test
//    void searchByMessageContent_findsMatchingMessages() {
//        List<Message> messages = messageRepository.searchByMessageContent("Hello");
//        assertEquals(1, messages.size());
//        assertEquals(message1.getMessage(), messages.get(0).getMessage());
//    }
//
//    @Test
//    void countBySession_returnsCorrectCount() {
//        Long count = messageRepository.countBySession(session.getSessionId());
//        assertEquals(2L, count);
//    }
//
//    @Test
//    void findBySessionIdWithRelations_returnsAllMessages() {
//        List<Message> messages = messageRepository.findBySessionIdWithRelations(session.getSessionId());
//        assertEquals(2, messages.size());
//        assertEquals(sender.getId(), messages.get(0).getSender().getId());
//    }
//
//    @Test
//    void findSessionMessagesWithPagination_returnsPagedMessages() {
//        List<Message> page = messageRepository.findSessionMessagesWithPagination(session.getSessionId(), 0, 1);
//        assertEquals(1, page.size());
//    }
//}
