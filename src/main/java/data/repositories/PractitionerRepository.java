package data.repositories;

import data.entities.Practitioner;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PractitionerRepository implements PanacheRepositoryBase<Practitioner, UUID> {

    public List<Practitioner> findAllPractitioners(int pageIndex, int pageSize) {
        return findAll().page(pageIndex, pageSize).list();
    }

    public List<Practitioner> findAllPractitionersWithRelations(int pageIndex, int pageSize) {
        List<Practitioner> practitioners = findAll().page(pageIndex, pageSize).list();
        practitioners.forEach(p -> {
            Hibernate.initialize(p.getConditions());
            Hibernate.initialize(p.getEncounters());
            Hibernate.initialize(p.getObservations());
        });
        return practitioners;
    }

    public Practitioner findById(UUID id) {
        return find("id", id).firstResult();
    }

    public Practitioner findByIdWithRelations(UUID id) {
        Practitioner practitioner = find("id", id).firstResult();
        if (practitioner == null) return null;
        Hibernate.initialize(practitioner.getConditions());
        Hibernate.initialize(practitioner.getEncounters());
        Hibernate.initialize(practitioner.getObservations());
        return practitioner;
    }

    public Practitioner findByEmail(String email) {
        return find("email = ?1", email).firstResult();
    }

    public Practitioner findByEmailWithRelations(String email) {
        Practitioner practitioner = find("email", email).firstResult();
        if (practitioner == null) return null;
        Hibernate.initialize(practitioner.getConditions());
        Hibernate.initialize(practitioner.getEncounters());
        Hibernate.initialize(practitioner.getObservations());
        return practitioner;
    }

    public List<Practitioner> findByOrganizationId(UUID organizationId) {
        return find("organization.organizationId = ?1", organizationId).list();
    }

    public List<Practitioner> findByOrganizationIdWithRelations(UUID organizationId) {
        List<Practitioner> practitioners = find("organization.organizationId = ?1", organizationId).list();
        practitioners.forEach(p -> {
            Hibernate.initialize(p.getConditions());
            Hibernate.initialize(p.getEncounters());
            Hibernate.initialize(p.getObservations());
        });
        return practitioners;
    }

    public Long countByOrganization(UUID organizationId) {
        return count("organization.organizationId = ?1", organizationId);
    }

}