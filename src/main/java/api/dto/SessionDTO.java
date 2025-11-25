package api.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionDTO {
    public UUID sessionId;
    public String subject;
    public LocalDateTime creationDate;
    public UUID senderId;
    public UUID receiverId;
    public List<MessageDTO> messages;

    public SessionDTO() {
        this.messages = new ArrayList<>();
    }

    public SessionDTO(UUID sessionId, String subject, LocalDateTime creationDate,
                      UUID senderId, UUID receiverId, List<MessageDTO> messages) {
        this.sessionId = sessionId;
        this.subject = subject;
        this.creationDate = creationDate;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messages = messages != null ? messages : new ArrayList<>();
    }
}
