package data.repositories;

import data.entities.Condition;
import core.enums.ConditionType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ConditionRepository implements PanacheRepositoryBase<Condition, UUID> {

    public List<Condition> findByPatientId(UUID patientId) {
        return find("patient.id", patientId).list();
    }

    public List<Condition> findByConditionType(ConditionType conditionType) {
        return find("conditionType", conditionType).list();
    }

    public List<Condition> findBySeverityLevel(int severityLevel) {
        return find("severityLevel", severityLevel).list();
    }

    public List<Condition> findByPractitionerId(UUID practitionerId) {
        return find("practitioner.id", practitionerId).list();
    }

    public List<Condition> findHighSeverityConditions() {
        return find("severityLevel >= ?1", 7).list();
    }

    public List<Condition> searchByName(String namePattern) {
        return find("conditionName like ?1", "%" + namePattern + "%").list();
    }

    public Long countByPatient(UUID patientId) {
        return count("patient.id", patientId);
    }

    public List<Condition> findPatientConditionsPaginated(UUID patientId, int pageIndex, int pageSize) {
        return find("patient.id", patientId).page(pageIndex, pageSize).list();
    }



    public List<Condition> findByPatientIdWithRelations(UUID patientId) {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.patient.id = ?1
            """, patientId).list();
    }

    public List<Condition> findByConditionTypeWithRelations(ConditionType conditionType) {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.conditionType = ?1
            """, conditionType).list();
    }

    public List<Condition> findBySeverityLevelWithRelations(int severityLevel) {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.severityLevel = ?1
            """, severityLevel).list();
    }

    public List<Condition> findByPractitionerIdWithRelations(UUID practitionerId) {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.practitioner.id = ?1
            """, practitionerId).list();
    }

    public List<Condition> findHighSeverityConditionsWithRelations() {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.severityLevel >= ?1
            """, 7).list();
    }

    public List<Condition> searchByNameWithRelations(String namePattern) {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.conditionName like ?1
            """, "%" + namePattern + "%").list();
    }

    public List<Condition> findPatientConditionsPaginatedWithRelations(UUID patientId, int pageIndex, int pageSize) {
        return find("""
                SELECT DISTINCT c
                FROM Condition c
                LEFT JOIN FETCH c.patient
                LEFT JOIN FETCH c.practitioner
                WHERE c.patient.id = ?1
            """, patientId).page(pageIndex, pageSize).list();
    }
}