package api.dto;

import java.util.UUID;
import core.enums.UserType;

public class UserLoginDTO {
    public String email;
    public String password;

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
