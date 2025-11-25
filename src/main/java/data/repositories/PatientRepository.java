package data.repositories;

import data.entities.Condition;
import data.entities.Patient;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PatientRepository implements PanacheRepositoryBase<Patient, UUID> {

    public Patient findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public Patient findByEmailWithRelations(String email) {
        Patient patient = find("email", email).firstResult();
        if (patient == null) return null;
        Hibernate.initialize(patient.getConditions());
        Hibernate.initialize(patient.getEncounters());
        Hibernate.initialize(patient.getObservations());
        return patient;
    }

    public Patient findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Patient findByIdWithRelations(UUID id) {
        Patient patient = find("id", id).firstResult();
        if (patient == null) return null;
        Hibernate.initialize(patient.getConditions());
        Hibernate.initialize(patient.getEncounters());
        Hibernate.initialize(patient.getObservations());
        return patient;
    }

    public List<Patient> findAllPatients(int pageIndex, int pageSize) {
        return findAll().page(pageIndex, pageSize).list();
    }

    public List<Patient> findAllPatientsWithRelations(int pageIndex, int pageSize) {
        List<Patient> patients = findAll().page(pageIndex, pageSize).list();
        patients.forEach(p -> {
            Hibernate.initialize(p.getConditions());
            Hibernate.initialize(p.getEncounters());
            Hibernate.initialize(p.getObservations());
        });
        return patients;
    }

    public List<Patient> searchByName(String namePattern, int pageIndex, int pageSize) {
        return find("fullName like ?1", "%" + namePattern + "%")
                .page(pageIndex, pageSize).list();
    }

    public List<Patient> searchByNameWithRelations(String namePattern, int pageIndex, int pageSize) {
        List<Patient> patients = find("fullName like ?1", "%" + namePattern + "%")
                .page(pageIndex, pageSize).list();
        patients.forEach(p -> {
            Hibernate.initialize(p.getConditions());
            Hibernate.initialize(p.getEncounters());
            Hibernate.initialize(p.getObservations());
        });
        return patients;
    }

}
