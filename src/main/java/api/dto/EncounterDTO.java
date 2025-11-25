package api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EncounterDTO {
    public UUID encounterId;
    public LocalDateTime encounterDate;
    public String description; // renamed from notes to match entity
    public UUID patientId;
    public UUID practitionerId;

    public EncounterDTO() {}

    public EncounterDTO(UUID encounterId, LocalDateTime encounterDate, String description,
                        UUID patientId, UUID practitionerId) {
        this.encounterId = encounterId;
        this.encounterDate = encounterDate;
        this.description = description;
        this.patientId = patientId;
        this.practitionerId = practitionerId;
    }
}
