package ua.scudy.server.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.scudy.server.entity.user.profile.TeacherDescription;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    private ScudyTeacher teacher;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    private ScudyStudent student;

    public Follow(ScudyTeacher teacher, ScudyStudent student) {
        this.teacher = teacher;
        this.student = student;
    }
}
