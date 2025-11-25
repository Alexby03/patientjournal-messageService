package api.dto;

import core.enums.ConditionType;
import java.time.LocalDate;
import java.util.UUID;

public class ConditionDTO {
    public UUID conditionId;
    public String conditionName;
    public ConditionType conditionType;
    public int severityLevel;
    public LocalDate diagnosedDate;
    public UUID practitionerId;
    public UUID patientId;

    public ConditionDTO() {}

    public ConditionDTO(UUID conditionId, String conditionName, ConditionType conditionType,
                        int severityLevel, LocalDate diagnosedDate, UUID practitionerId, UUID patientId) {
        this.conditionId = conditionId;
        this.conditionName = conditionName;
        this.conditionType = conditionType;
        this.severityLevel = severityLevel;
        this.diagnosedDate = diagnosedDate;
        this.practitionerId = practitionerId;
        this.patientId = patientId;
    }
}
