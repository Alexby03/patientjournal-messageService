package api.dto;

import java.util.UUID;

public class PractitionerCreateDTO {
    public String fullName;
    public String email;
    public String password;
    public UUID organizationId;

    public PractitionerCreateDTO() {}

    public PractitionerCreateDTO(String fullName, String email, String password, UUID organizationId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.organizationId = organizationId;
    }
}
