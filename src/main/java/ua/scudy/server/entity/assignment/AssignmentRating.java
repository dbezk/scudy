package ua.scudy.server.entity.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.scudy.server.constants.SolutionResult;
import ua.scudy.server.entity.user.ScudyStudent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "assignments_ratings")
public class AssignmentRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private ScudyStudent senderOfRating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Assignment assignment;

    @Column(name = "rating")
    private int solutionRating;

    public AssignmentRating(ScudyStudent senderOfRating, Assignment assignment, int solutionRating) {
        this.senderOfRating = senderOfRating;
        this.assignment = assignment;
        this.solutionRating = solutionRating;
    }
}
