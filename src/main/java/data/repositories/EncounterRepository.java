package data.repositories;

import data.entities.Encounter;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EncounterRepository implements PanacheRepositoryBase<Encounter, UUID> {

    /**
     * Find encounters by patient ID (lazy relations)
     */
    public List<Encounter> findByPatientId(UUID patientId) {
        return find("patient.id", patientId).list();
    }

    /**
     * Find encounters by practitioner ID (lazy relations)
     */
    public List<Encounter> findByPractitionerId(UUID practitionerId) {
        return find("practitioner.id", practitionerId).list();
    }

    /**
     * Find encounters within a date range (lazy relations)
     */
    public List<Encounter> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return find("encounterDate between ?1 and ?2", startDate, endDate).list();
    }

    /**
     * Get recent encounters (last 30 days) (lazy relations)
     */
    public List<Encounter> findRecentEncounters() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return find("encounterDate >= ?1", thirtyDaysAgo).list();
    }

    /**
     * Search encounters by description (lazy relations)
     */
    public List<Encounter> searchByDescription(String descriptionPattern) {
        return find("description like ?1", "%" + descriptionPattern + "%").list();
    }

    /**
     * Count encounters for a patient
     */
    public Long countByPatient(UUID patientId) {
        return count("patient.id", patientId);
    }

    /**
     * Get encounters with pagination (lazy relations)
     */
    public List<Encounter> findPatientEncountersPaginated(UUID patientId, int pageIndex, int pageSize) {
        return find("patient.id", patientId).page(pageIndex, pageSize).list();
    }




    /**
     * Find encounters by patient ID with patient and practitioner eagerly loaded
     */
    public List<Encounter> findByPatientIdWithRelations(UUID patientId) {
        return find("""
                SELECT DISTINCT e
                FROM Encounter e
                LEFT JOIN FETCH e.patient
                LEFT JOIN FETCH e.practitioner
                WHERE e.patient.id = ?1
            """, patientId).list();
    }

    /**
     * Find encounters by practitioner ID with patient and practitioner eagerly loaded
     */
    public List<Encounter> findByPractitionerIdWithRelations(UUID practitionerId) {
        return find("""
                SELECT DISTINCT e
                FROM Encounter e
                LEFT JOIN FETCH e.patient
                LEFT JOIN FETCH e.practitioner
                WHERE e.practitioner.id = ?1
            """, practitionerId).list();
    }

    /**
     * Find encounters within a date range with relations eagerly loaded
     */
    public List<Encounter> findByDateRangeWithRelations(LocalDateTime startDate, LocalDateTime endDate) {
        return find("""
                SELECT DISTINCT e
                FROM Encounter e
                LEFT JOIN FETCH e.patient
                LEFT JOIN FETCH e.practitioner
                WHERE e.encounterDate BETWEEN ?1 AND ?2
            """, startDate, endDate).list();
    }

    /**
     * Get recent encounters (last 30 days) with relations eagerly loaded
     */
    public List<Encounter> findRecentEncountersWithRelations() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return find("""
                SELECT DISTINCT e
                FROM Encounter e
                LEFT JOIN FETCH e.patient
                LEFT JOIN FETCH e.practitioner
                WHERE e.encounterDate >= ?1
            """, thirtyDaysAgo).list();
    }

    /**
     * Search encounters by description with relations eagerly loaded
     */
    public List<Encounter> searchByDescriptionWithRelations(String descriptionPattern) {
        return find("""
                SELECT DISTINCT e
                FROM Encounter e
                LEFT JOIN FETCH e.patient
                LEFT JOIN FETCH e.practitioner
                WHERE e.description like ?1
            """, "%" + descriptionPattern + "%").list();
    }

    /**
     * Get paginated encounters for a patient with relations eagerly loaded
     */
    public List<Encounter> findPatientEncountersPaginatedWithRelations(UUID patientId, int pageIndex, int pageSize) {
        return find("""
                SELECT DISTINCT e
                FROM Encounter e
                LEFT JOIN FETCH e.patient
                LEFT JOIN FETCH e.practitioner
                WHERE e.patient.id = ?1
            """, patientId).page(pageIndex, pageSize).list();
    }
}