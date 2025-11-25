package data.repositories;

import data.entities.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MessageRepository implements PanacheRepositoryBase<Message, UUID> {

    public List<Message> findBySessionId(UUID sessionId) {
        return find("sessionId", sessionId).list();
    }

    public List<Message> findBySenderId(UUID senderId) {
        return find("senderId", senderId).list();
    }

    public List<Message> searchByMessageContent(String contentPattern) {
        return find("message like ?1", "%" + contentPattern + "%").list();
    }

    public List<Message> findSessionMessagesWithPagination(UUID sessionId, int pageIndex, int pageSize) {
        return find("sessionId", sessionId)
                .page(pageIndex, pageSize)
                .list();
    }

    public Long countBySession(UUID sessionId) {
        return count("sessionId", sessionId);
    }

    public Message findLatestMessageInSession(UUID sessionId) {
        return find("sessionId = ?1 order by dateTime desc", sessionId)
                .firstResult();
    }


    public List<Message> findBySessionIdWithRelations(UUID sessionId) {
        return find("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.session
                LEFT JOIN FETCH m.sender
                WHERE m.sessionId = ?1
            """, sessionId).list();
    }

    public List<Message> findBySenderIdWithRelations(UUID senderId) {
        return find("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.session
                LEFT JOIN FETCH m.sender
                WHERE m.senderId = ?1
            """, senderId).list();
    }

    public List<Message> searchByMessageContentWithRelations(String contentPattern) {
        return find("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.session
                LEFT JOIN FETCH m.sender
                WHERE m.message LIKE ?1
            """, "%" + contentPattern + "%").list();
    }

    public List<Message> findSessionMessagesWithPaginationWithRelations(UUID sessionId, int pageIndex, int pageSize) {
        return find("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.session
                LEFT JOIN FETCH m.sender
                WHERE m.sessionId = ?1
            """, sessionId)
                .page(pageIndex, pageSize)
                .list();
    }

    public Message findLatestMessageInSessionWithRelations(UUID sessionId) {
        return find("""
                SELECT m FROM Message m
                LEFT JOIN FETCH m.session
                LEFT JOIN FETCH m.sender
                WHERE m.sessionId = ?1
                ORDER BY m.dateTime DESC
            """, sessionId)
                .firstResult();
    }
}
