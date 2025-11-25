package api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientDTO {
    public UUID id;
    public String fullName;
    public String email;

    public List<ConditionDTO> conditions;
    public List<EncounterDTO> encounters;
    public List<ObservationDTO> observations;

    public PatientDTO() {
        this.conditions = new ArrayList<>();
        this.encounters = new ArrayList<>();
        this.observations = new ArrayList<>();
    }

    public PatientDTO(UUID id, String fullName, String email,
                      List<ConditionDTO> conditions,
                      List<EncounterDTO> encounters,
                      List<ObservationDTO> observations) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.conditions = conditions != null ? conditions : new ArrayList<>();
        this.encounters = encounters != null ? encounters : new ArrayList<>();
        this.observations = observations != null ? observations : new ArrayList<>();
    }
}
