package ua.scudy.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.scudy.server.constants.RoleType;
import ua.scudy.server.entity.user.ScudyRole;

@Repository
public interface RoleRepo extends JpaRepository<ScudyRole, Long> {

    ScudyRole findByName(RoleType role);

}
