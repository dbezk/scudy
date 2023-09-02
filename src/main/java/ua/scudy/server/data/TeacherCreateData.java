package ua.scudy.server.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TeacherCreateData {

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("education_info")
    private String education;

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    @JsonProperty("profile_description")
    private String profileDescription;

    @NotNull
    @JsonProperty("experience_description")
    private String experienceDescription;

}
