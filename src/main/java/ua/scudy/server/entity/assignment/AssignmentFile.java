package ua.scudy.server.entity.assignment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "assignments_files")
public class AssignmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public AssignmentFile(String name) {
        this.name = name;
    }
}
