package api.dto;

import core.enums.LocationType;
import core.enums.OrganizationType;
import java.util.UUID;

public class OrganizationDTO {
    public UUID organizationId;
    public OrganizationType organizationType;
    public LocationType locationType;

    public OrganizationDTO() {}

    public OrganizationDTO(UUID organizationId, OrganizationType organizationType, LocationType locationType) {
        this.organizationId = organizationId;
        this.organizationType = organizationType;
        this.locationType = locationType;
    }
}
