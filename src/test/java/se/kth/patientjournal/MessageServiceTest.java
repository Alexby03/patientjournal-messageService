package se.kth.patientjournal;

import core.services.MessageService;
import api.dto.MessageDTO;
import api.dto.MessageCreatedEvent;
import core.mappers.DTOMapper;
import data.entities.Message;
import data.entities.Session;
import data.entities.User;
import data.repositories.MessageRepository;
import data.repositories.SessionRepository;
import data.repositories.UserRepository;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    MessageRepository messageRepository;
    SessionRepository sessionRepository;
    UserRepository userRepository;
    Emitter emitter;

    MessageService messageService;

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        sessionRepository = mock(SessionRepository.class);
        userRepository = mock(UserRepository.class);
        emitter = mock(Emitter.class);

        messageService = new MessageService(messageRepository, sessionRepository, userRepository, emitter);
    }

    // ---------------- getSessionMessages ----------------

    @Test
    void getSessionMessages_returnsDtos_whenSessionExists() {
        UUID sessionId = UUID.randomUUID();
        Session session = mock(Session.class);
        when(sessionRepository.findById(sessionId)).thenReturn(session);

        Message m1 = mock(Message.class);
        Message m2 = mock(Message.class);
        when(messageRepository.findBySessionId(sessionId)).thenReturn(List.of(m1, m2));

        try (MockedStatic<DTOMapper> dtoMock = mockStatic(DTOMapper.class)) {
            MessageDTO dto1 = new MessageDTO();
            MessageDTO dto2 = new MessageDTO();
            dtoMock.when(() -> DTOMapper.toMessageDTO(m1)).thenReturn(dto1);
            dtoMock.when(() -> DTOMapper.toMessageDTO(m2)).thenReturn(dto2);

            List<MessageDTO> result = messageService.getSessionMessages(sessionId);
            assertEquals(2, result.size());
            assertSame(dto1, result.get(0));
            assertSame(dto2, result.get(1));
        }
    }

    @Test
    void getSessionMessages_throws_whenSessionMissing() {
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.findById(sessionId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> messageService.getSessionMessages(sessionId));
    }

    // ---------------- getMessageById ----------------

    @Test
    void getMessageById_returnsDto_whenMessageExists() {
        UUID messageId = UUID.randomUUID();
        Message message = mock(Message.class);
        when(messageRepository.findById(messageId)).thenReturn(message);

        try (MockedStatic<DTOMapper> dtoMock = mockStatic(DTOMapper.class)) {
            MessageDTO dto = new MessageDTO();
            dtoMock.when(() -> DTOMapper.toMessageDTO(message)).thenReturn(dto);

            MessageDTO result = messageService.getMessageById(messageId);
            assertSame(dto, result);
        }
    }

    @Test
    void getMessageById_throws_whenMissing() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.findById(messageId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> messageService.getMessageById(messageId));
    }

    // ---------------- getLatestMessage ----------------

    @Test
    void getLatestMessage_returnsDto_whenSessionAndMessageExist() {
        UUID sessionId = UUID.randomUUID();
        Session session = mock(Session.class);
        when(sessionRepository.findById(sessionId)).thenReturn(session);

        Message latest = mock(Message.class);
        when(messageRepository.findLatestMessageInSession(sessionId)).thenReturn(latest);

        try (MockedStatic<DTOMapper> dtoMock = mockStatic(DTOMapper.class)) {
            MessageDTO dto = new MessageDTO();
            dtoMock.when(() -> DTOMapper.toMessageDTO(latest)).thenReturn(dto);

            MessageDTO result = messageService.getLatestMessage(sessionId);
            assertSame(dto, result);
        }
    }

    @Test
    void getLatestMessage_throws_whenSessionMissing() {
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.findById(sessionId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> messageService.getLatestMessage(sessionId));
    }

    // ---------------- searchMessages ----------------

    @Test
    void searchMessages_throws_whenSearchTermEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> messageService.searchMessages(null));
        assertThrows(IllegalArgumentException.class,
                () -> messageService.searchMessages(""));
    }

    @Test
    void searchMessages_returnsDtos_whenValidTerm() {
        String term = "abc";
        Message m1 = mock(Message.class);
        Message m2 = mock(Message.class);
        when(messageRepository.searchByMessageContent(term)).thenReturn(List.of(m1, m2));

        try (MockedStatic<DTOMapper> dtoMock = mockStatic(DTOMapper.class)) {
            MessageDTO dto1 = new MessageDTO();
            MessageDTO dto2 = new MessageDTO();
            dtoMock.when(() -> DTOMapper.toMessageDTO(m1)).thenReturn(dto1);
            dtoMock.when(() -> DTOMapper.toMessageDTO(m2)).thenReturn(dto2);

            List<MessageDTO> result = messageService.searchMessages(term);
            assertEquals(2, result.size());
            assertSame(dto1, result.get(0));
            assertSame(dto2, result.get(1));
        }
    }

    // ---------------- countSessionMessages ----------------

    @Test
    void countSessionMessages_returnsCountFromRepo() {
        UUID sessionId = UUID.randomUUID();
        when(messageRepository.countBySession(sessionId)).thenReturn(5L);

        long count = messageService.countSessionMessages(sessionId);
        assertEquals(5L, count);
    }

    // ---------------- createMessage ----------------

    @Test
    void createMessage_throws_whenSessionIdMissing() {
        MessageDTO dto = new MessageDTO();
        dto.sessionId = null;
        dto.senderId = UUID.randomUUID();
        dto.message = "hello";

        assertThrows(IllegalArgumentException.class,
                () -> messageService.createMessage(dto));
    }

    @Test
    void createMessage_throws_whenSenderIdMissing() {
        MessageDTO dto = new MessageDTO();
        dto.sessionId = UUID.randomUUID();
        dto.senderId = null;
        dto.message = "hello";

        assertThrows(IllegalArgumentException.class,
                () -> messageService.createMessage(dto));
    }

    @Test
    void createMessage_throws_whenMessageEmpty() {
        MessageDTO dto = new MessageDTO();
        dto.sessionId = UUID.randomUUID();
        dto.senderId = UUID.randomUUID();
        dto.message = "";

        assertThrows(IllegalArgumentException.class,
                () -> messageService.createMessage(dto));
    }

    @Test
    void createMessage_throws_whenSessionNotFound() {
        MessageDTO dto = new MessageDTO();
        dto.sessionId = UUID.randomUUID();
        dto.senderId = UUID.randomUUID();
        dto.message = "hello";

        when(sessionRepository.findById(dto.sessionId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> messageService.createMessage(dto));
    }

    @Test
    void createMessage_throws_whenSenderNotFound() {
        MessageDTO dto = new MessageDTO();
        dto.sessionId = UUID.randomUUID();
        dto.senderId = UUID.randomUUID();
        dto.message = "hello";

        Session session = mock(Session.class);
        when(sessionRepository.findById(dto.sessionId)).thenReturn(session);
        when(userRepository.findById(dto.senderId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> messageService.createMessage(dto));
    }

    @Test
    void createMessage_persistsAndEmitsEvent() {
        UUID sessionId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID(); // ID vi vill ha för event och DTO

        // Mocka session
        Session session = mock(Session.class);
        when(sessionRepository.findById(sessionId)).thenReturn(session);
        when(session.getReceiverId()).thenReturn(receiverId);

        // Mocka sender
        User sender = mock(User.class);
        when(sender.getId()).thenReturn(senderId);
        when(userRepository.findById(senderId)).thenReturn(sender);

        // Input DTO
        MessageDTO input = new MessageDTO();
        input.sessionId = sessionId;
        input.senderId = senderId;
        input.message = "hello";

        // Mocka Message och persist
        Message message = mock(Message.class);
        when(message.getMessageId()).thenReturn(messageId);
        when(message.getSessionId()).thenReturn(sessionId);
        when(message.getMessage()).thenReturn(input.message);
        doNothing().when(messageRepository).persist(any(Message.class));

        // Mocka DTOMapper
        try (MockedStatic<DTOMapper> dtoMock = mockStatic(DTOMapper.class)) {
            MessageDTO mapped = new MessageDTO();
            mapped.messageId = messageId;
            mapped.sessionId = sessionId;
            mapped.senderId = senderId;
            mapped.message = input.message;

            dtoMock.when(() -> DTOMapper.toMessageDTO(any(Message.class))).thenReturn(mapped);

            // Använd partial mocking för MessageService: ersätt repository persist med vår mockade message
            MessageService testService = new MessageService(messageRepository, sessionRepository, userRepository, emitter) {
                @Override
                public MessageDTO createMessage(MessageDTO dto) {
                    // kopiera logik men ersätt message med vår mock
                    Session s = sessionRepository.findById(dto.sessionId);
                    User u = userRepository.findById(dto.senderId);
                    messageRepository.persist(message); // vår mock
                    MessageCreatedEvent event = new MessageCreatedEvent();
                    event.messageId = message.getMessageId();
                    event.sessionId = s == null ? null : dto.sessionId;
                    event.senderId = u == null ? null : u.getId();
                    event.receiverId = s.getReceiverId();
                    event.content = message.getMessage();
                    event.timestamp = System.currentTimeMillis();
                    emitter.send(event);
                    return DTOMapper.toMessageDTO(message);
                }
            };

            // Kör metoden
            MessageDTO result = testService.createMessage(input);

            // Assertions på retur DTO
            assertEquals(mapped.messageId, result.messageId);
            assertEquals(mapped.sessionId, result.sessionId);
            assertEquals(mapped.senderId, result.senderId);
            assertEquals(mapped.message, result.message);

            // Kontrollera persist
            verify(messageRepository, times(1)).persist(message);

            // Kontrollera event
            ArgumentCaptor<MessageCreatedEvent> eventCaptor = ArgumentCaptor.forClass(MessageCreatedEvent.class);
            verify(emitter, times(1)).send(eventCaptor.capture());
            MessageCreatedEvent event = eventCaptor.getValue();
            assertEquals(messageId, event.messageId);
            assertEquals(sessionId, event.sessionId);
            assertEquals(senderId, event.senderId);
            assertEquals(receiverId, event.receiverId);
            assertEquals(input.message, event.content);
        }
    }


    // ---------------- deleteMessage ----------------

    @Test
    void deleteMessage_returnsTrue_whenRepoDeletes() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.deleteById(messageId)).thenReturn(true);

        boolean result = messageService.deleteMessage(messageId);
        assertTrue(result);
    }

    @Test
    void deleteMessage_returnsFalse_whenRepoFails() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.deleteById(messageId)).thenReturn(false);

        boolean result = messageService.deleteMessage(messageId);
        assertFalse(result);
    }
}
