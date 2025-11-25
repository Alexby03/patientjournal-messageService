package api.dto;

import java.util.UUID;
import core.enums.UserType;

public class UserDTO {
    public UUID id;
    public String fullName;
    public String email;
    public UserType userType;

    public UserDTO() {}

    public UserDTO(UUID id, String fullName, String email, UserType userType) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
    }
}