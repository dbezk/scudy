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
@Table(name = "assignments_solutions")
public class AssignmentSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @OneToOne(fetch = FetchType.EAGER)
    private ScudyStudent senderOfSolution;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Assignment assignment;

    @Column(name = "solution_result")
    @Enumerated(EnumType.STRING)
    private SolutionResult solutionResult;

    public AssignmentSolution(ScudyStudent senderOfSolution, Assignment assignment, SolutionResult solutionResult) {
        this.receivedAt = LocalDateTime.now();
        this.senderOfSolution = senderOfSolution;
        this.assignment = assignment;
        this.solutionResult = solutionResult;
    }
}
