package data.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID messageId;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private Session session;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    public Message() {}

    public Message(Session session, User sender, String messageContent) {
        this.session = session;
        this.sender = sender;
        this.sessionId = session != null ? session.getSessionId() : null;
        this.senderId = sender != null ? sender.getId() : null;
        this.message = messageContent;
        this.dateTime = LocalDateTime.now();
    }

    public UUID getMessageId() {
        return messageId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public Session getSessionEntity() {
        return session;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setSession(Session session) {
        this.session = session;
        this.sessionId = session != null ? session.getSessionId() : null;
    }

    public void setSender(User sender) {
        this.sender = sender;
        this.senderId = sender != null ? sender.getId() : null;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", sessionId=" + sessionId +
                ", senderId=" + senderId +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
