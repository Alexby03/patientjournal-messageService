package api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ObservationDTO {
    public UUID observationId;
    public String description;
    public LocalDateTime observationDate;
    public UUID patientId;
    public UUID practitionerId;

    public ObservationDTO() {}

    public ObservationDTO(UUID observationId, String description, LocalDateTime observationDate,
                          UUID patientId, UUID practitionerId) {
        this.observationId = observationId;
        this.description = description;
        this.observationDate = observationDate;
        this.patientId = patientId;
        this.practitionerId = practitionerId;
    }
}
