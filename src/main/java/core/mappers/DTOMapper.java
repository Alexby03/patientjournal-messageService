package core.mappers;

import api.dto.*;
import data.entities.*;
import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    // Message
    public static MessageDTO toMessageDTO(Message message) {
        return new MessageDTO(
                message.getMessageId(),
                message.getSessionId(),
                message.getSenderId(),
                message.getMessage(),
                message.getDateTime()
        );
    }

    // Session
    public static SessionDTO toSessionDTO(Session session, boolean eagerMessages) {
        SessionDTO dto = new SessionDTO();
        dto.sessionId = session.getSessionId();
        dto.subject = session.getSubject();
        dto.creationDate = session.getCreationDate();
        dto.senderId = session.getSenderId();
        dto.receiverId = session.getReceiverId();

        if (eagerMessages && session.getMessages() != null) {
            dto.messages = session.getMessages().stream()
                    .map(DTOMapper::toMessageDTO)
                    .collect(Collectors.toList());
        }

        return dto;
    }
}
