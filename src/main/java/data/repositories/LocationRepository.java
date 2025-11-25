package data.repositories;

import data.entities.Location;
import core.enums.LocationType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LocationRepository implements PanacheRepositoryBase<Location, UUID> {

    /**
     * Find locations by type
     */
    public Location findByLocationType(LocationType locationType) {
        return find("locationType", locationType).firstResult();
    }

    /**
     * Get all locations
     */
    public List<Location> findAllLocations() {
        return findAll().list();
    }

    /**
     * Count total locations
     */
    public Long countTotalLocations() {
        return count();
    }
}
