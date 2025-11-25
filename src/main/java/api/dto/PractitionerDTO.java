package api.dto;

import core.enums.UserType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PractitionerDTO {
    public UUID id;
    public String fullName;
    public String email;
    public UserType userType;
    public String organizationType;

    public List<ConditionDTO> conditions;
    public List<EncounterDTO> encounters;
    public List<ObservationDTO> observations;

    public PractitionerDTO() {
        this.conditions = new ArrayList<>();
        this.encounters = new ArrayList<>();
        this.observations = new ArrayList<>();
    }

    public PractitionerDTO(UUID id, String fullName, String email, UserType userType, String organizationType,
                           List<ConditionDTO> conditions, List<EncounterDTO> encounters, List<ObservationDTO> observations) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.organizationType = organizationType;
        this.conditions = conditions != null ? conditions : new ArrayList<>();
        this.encounters = encounters != null ? encounters : new ArrayList<>();
        this.observations = observations != null ? observations : new ArrayList<>();
    }
}
