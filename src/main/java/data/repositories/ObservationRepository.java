package data.repositories;

import data.entities.Observation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ObservationRepository implements PanacheRepositoryBase<Observation, UUID> {

    public List<Observation> findByPatientId(UUID patientId) {
        return find("patient.id", patientId).list();
    }

    public List<Observation> findByPractitionerId(UUID practitionerId) {
        return find("practitioner.id", practitionerId).list();
    }

    public List<Observation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return find("observationDate between ?1 and ?2", startDate, endDate).list();
    }

    public List<Observation> searchByDescription(String descriptionPattern) {
        return find("description like ?1", "%" + descriptionPattern + "%").list();
    }

    public Long countByPatient(UUID patientId) {
        return count("patient.id", patientId);
    }

    public Observation findMostRecentByPatient(UUID patientId) {
        List<Observation> list = find("patient.id", patientId).list();
        return list.stream()
                .max(Comparator.comparing(Observation::getObservationDate))
                .orElse(null);
    }

    public List<Observation> findByPatientIdWithRelations(UUID patientId) {
        return find("""
                SELECT o FROM Observation o
                LEFT JOIN FETCH o.patient
                LEFT JOIN FETCH o.practitioner
                WHERE o.patient.id = ?1
            """, patientId).list();
    }

    public List<Observation> findByPractitionerIdWithRelations(UUID practitionerId) {
        return find("""
                SELECT o FROM Observation o
                LEFT JOIN FETCH o.patient
                LEFT JOIN FETCH o.practitioner
                WHERE o.practitioner.id = ?1
            """, practitionerId).list();
    }

    public List<Observation> findByDateRangeWithRelations(LocalDateTime startDate, LocalDateTime endDate) {
        return find("""
                SELECT o FROM Observation o
                LEFT JOIN FETCH o.patient
                LEFT JOIN FETCH o.practitioner
                WHERE o.observationDate BETWEEN ?1 AND ?2
            """, startDate, endDate).list();
    }

    public List<Observation> searchByDescriptionWithRelations(String descriptionPattern) {
        return find("""
                SELECT o FROM Observation o
                LEFT JOIN FETCH o.patient
                LEFT JOIN FETCH o.practitioner
                WHERE o.description LIKE ?1
            """, "%" + descriptionPattern + "%").list();
    }

    public Observation findMostRecentByPatientWithRelations(UUID patientId) {
        return find("""
                SELECT o FROM Observation o
                LEFT JOIN FETCH o.patient
                LEFT JOIN FETCH o.practitioner
                WHERE o.patient.id = ?1
                ORDER BY o.observationDate DESC
            """, patientId)
                .firstResult();
    }

    public List<Observation> findPatientObservationsPaginated(UUID patientId, int pageIndex, int pageSize) {
        return find("patient.id", patientId)
                .page(pageIndex, pageSize)
                .list();
    }

    public List<Observation> findPatientObservationsPaginatedWithRelations(UUID patientId, int pageIndex, int pageSize) {
        return find("""
                SELECT o FROM Observation o
                LEFT JOIN FETCH o.patient
                LEFT JOIN FETCH o.practitioner
                WHERE o.patient.id = ?1
            """, patientId)
                .page(pageIndex, pageSize)
                .list();
    }
}
