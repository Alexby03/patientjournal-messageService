package api.dto;

import core.enums.LocationType;
import core.enums.OrganizationType;
import java.util.UUID;

public class LocationDTO {
    public UUID locationId;
    public LocationType locationType;

    public LocationDTO() {}

    public LocationDTO(UUID locationId, LocationType locationType) {
        this.locationId = locationId;
        this.locationType = locationType;
    }
}
