package ua.scudy.server.entity.user.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data
@NoArgsConstructor
@Table(name = "teacher_profiles_info")
public class TeacherDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "profile_description")
    private String profileDescription;

    @Column(name = "experience_description")
    private String experienceDescription;

    public TeacherDescription(String profileDescription, String experienceDescription) {
        this.profileDescription = profileDescription;
        this.experienceDescription = experienceDescription;
    }
}
