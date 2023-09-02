package ua.scudy.server.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ua.scudy.server.entity.assignment.Assignment;
import ua.scudy.server.entity.user.profile.TeacherDescription;
import ua.scudy.server.entity.user.profile.TeacherProfileStatistics;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity @Data @EqualsAndHashCode
@NoArgsConstructor
@Table(name = "teachers")
public class ScudyTeacher {


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "education_info", nullable = false)
    private String education;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "activated", nullable = false)
    private boolean activated = false;

    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<ScudyRole> scudyRoles;

    @OneToOne(cascade = CascadeType.ALL)
    private TeacherDescription teacherDescription;

    @OneToOne(cascade = CascadeType.ALL)
    private TeacherProfileStatistics teacherProfileStatistics;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<Assignment> assignmentList = new ArrayList<>();

    public ScudyTeacher(String firstName, String lastName, String education,
                        String email, String password,
                        Collection<ScudyRole> scudyRoles, TeacherDescription teacherDescription,
                        TeacherProfileStatistics teacherProfileStatistics) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.education = education;
        this.email = email;
        this.password = password;
        this.scudyRoles = scudyRoles;
        this.teacherDescription = teacherDescription;
        this.teacherProfileStatistics = teacherProfileStatistics;
    }
}
