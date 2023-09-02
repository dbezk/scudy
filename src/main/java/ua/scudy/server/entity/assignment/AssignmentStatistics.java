package ua.scudy.server.entity.assignment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "assignments_statistics")
public class AssignmentStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "done_solutions_count", nullable = false)
    private int doneSolutionsCount = 0;

    @Column(name = "failed_solutions_count", nullable = false)
    private int failedSolutionsCount = 0;

}
