package api.dto;

public class PatientCreateDTO {
    public String fullName;
    public String email;
    public String password;

    public PatientCreateDTO() {}

    public PatientCreateDTO(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
