package ua.scudy.server.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.scudy.server.constants.RoleType;

import javax.persistence.*;

@Entity @Data
@NoArgsConstructor
@Table(name = "roles")
public class ScudyRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;

    public ScudyRole(RoleType name) {
        this.name = name;
    }

}
