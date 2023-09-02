package ua.scudy.server.entity.user.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teacher_profiles_statistics")
public class TeacherProfileStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "assignments_count")
    private Integer assignmentsCount = 0;

    @Column(name = "followers_count")
    private Integer followersCount = 0;

}
