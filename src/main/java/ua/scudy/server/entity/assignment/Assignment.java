package ua.scudy.server.entity.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.scudy.server.entity.user.ScudyTeacher;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity @Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @OneToOne(fetch = FetchType.EAGER)
    private AssignmentFile assignmentFile;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AssignmentStatistics assignmentStatistics;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AssignmentSolution> assignmentSolutions;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AssignmentRating> assignmentRatings;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private ScudyTeacher teacher;

    public Assignment(String title, String description, AssignmentFile assignmentFile) {
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.assignmentFile = assignmentFile;
        this.assignmentStatistics = new AssignmentStatistics();
    }
}
