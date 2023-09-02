package ua.scudy.server.entity.user.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data
@NoArgsConstructor
@Table(name = "student_profiles_statistics")
public class StudentProfileStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "done_solutions_count")
    private Integer doneSolutions = 0;

    @Column(name = "failed_solutions_count")
    private Integer failedSolutions = 0;

    @Column(name = "follows_count")
    private Integer followsCount = 0;

}
