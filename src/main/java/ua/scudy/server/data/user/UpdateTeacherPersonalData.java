package ua.scudy.server.data.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateTeacherPersonalData {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String education;
    @JsonProperty("profile_description")
    private String profileDescription;
    @JsonProperty("experience_description")
    private String experienceDescription;
    @Email
    private String email;
    private String password;

}