package ua.scudy.server.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.scudy.server.entity.assignment.AssignmentSolution;
import ua.scudy.server.entity.user.profile.StudentProfileStatistics;
import ua.scudy.server.entity.user.profile.TeacherDescription;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity @Data
@NoArgsConstructor
@Table(name = "students")
public class ScudyStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private StudentProfileStatistics studentProfileStatistics;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AssignmentSolution> studentSolutions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followList = new ArrayList<>();

    public ScudyStudent(String firstName, String lastName, String education,
                        String email, String password,
                        Collection<ScudyRole> scudyRoles, StudentProfileStatistics studentProfileStatistics) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.education = education;
        this.email = email;
        this.password = password;
        this.scudyRoles = scudyRoles;
        this.studentProfileStatistics = studentProfileStatistics;
    }

}
