package data.repositories;

import data.entities.Organization;
import core.enums.OrganizationType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepositoryBase<Organization, UUID> {

    /**
     * Find organizations by type (lazy)
     */
    public List<Organization> findByOrganizationType(OrganizationType organizationType) {
        return find("organizationType", organizationType).list();
    }

    /**
     * Find organizations by type with practitioners eagerly fetched
     */
    public List<Organization> findByOrganizationTypeWithPractitioners(OrganizationType organizationType) {
        return find("""
                SELECT DISTINCT o
                FROM Organization o
                LEFT JOIN FETCH o.practitioners
                WHERE o.organizationType = ?1
            """, organizationType).list();
    }

    /**
     * Get all organizations with pagination (lazy)
     */
    public List<Organization> findAllOrganizations(int pageIndex, int pageSize) {
        return listAll().subList(pageIndex * pageSize,
                Math.min((pageIndex + 1) * pageSize, (int) count()));
    }

    /**
     * Get all organizations with practitioners eagerly loaded
     */
    public List<Organization> findAllOrganizationsWithPractitioners(int pageIndex, int pageSize) {
        return find("""
                SELECT DISTINCT o
                FROM Organization o
                LEFT JOIN FETCH o.practitioners
            """)
                .page(pageIndex, pageSize)
                .list();
    }

    /**
     * Count total organizations
     */
    public Long countTotalOrganizations() {
        return count();
    }

    /**
     * Find organization by ID (lazy)
     */
    public Organization findById(UUID id) {
        return find("organizationId", id).firstResult();
    }

    /**
     * Find organization by ID with practitioners eagerly loaded
     */
    public Organization findByIdWithPractitioners(UUID id) {
        return find("""
                SELECT DISTINCT o
                FROM Organization o
                LEFT JOIN FETCH o.practitioners
                WHERE o.organizationId = ?1
            """, id).firstResult();
    }
}
