package data.repositories;

import data.entities.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SessionRepository implements PanacheRepositoryBase<Session, UUID> {

    public List<Session> findBySenderId(UUID senderId) {
        return find("senderId", senderId).list();
    }

    public List<Session> findByReceiverId(UUID receiverId) {
        return find("receiverId", receiverId).list();
    }

    public List<Session> findSessionsBetweenUsers(UUID userId1, UUID userId2) {
        return find("(senderId = ?1 and receiverId = ?2) or (senderId = ?2 and receiverId = ?1)",
                userId1, userId2).list();
    }

    public List<Session> searchBySubject(String subjectPattern) {
        return find("subject like ?1", "%" + subjectPattern + "%").list();
    }

    public List<Session> findAllUserSessions(UUID userId) {
        return find("senderId = ?1 or receiverId = ?1", userId).list();
    }

    public Long countUserSessions(UUID userId) {
        return count("senderId = ?1 or receiverId = ?1", userId);
    }

    public List<Session> findSessionsWithPagination(UUID userId, int pageIndex, int pageSize) {
        return find("senderId = ?1 or receiverId = ?1", userId)
                .page(pageIndex, pageSize)
                .list();
    }

    public Session findByIdSimple(UUID sessionId) {
        return findById(sessionId);
    }

    public Session findByIdWithMessages(UUID sessionId) {
        return find("SELECT s FROM Session s LEFT JOIN FETCH s.messages WHERE s.sessionId = ?1", sessionId)
                .firstResult();
    }

    public List<Session> findBySenderIdWithMessages(UUID senderId) {
        return find("""
                SELECT DISTINCT s
                FROM Session s
                LEFT JOIN FETCH s.messages
                WHERE s.senderId = ?1
            """, senderId)
                .list();
    }

    public List<Session> findByReceiverIdWithMessages(UUID receiverId) {
        return find("""
                SELECT DISTINCT s
                FROM Session s
                LEFT JOIN FETCH s.messages
                WHERE s.receiverId = ?1
            """, receiverId)
                .list();
    }

    public List<Session> findAllUserSessionsWithMessages(UUID userId) {
        return find("""
                SELECT DISTINCT s
                FROM Session s
                LEFT JOIN FETCH s.messages
                WHERE s.senderId = ?1 OR s.receiverId = ?1
            """, userId)
                .list();
    }

}