package ua.scudy.server.repository.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.assignment.AssignmentFile;

@Repository
public interface AssignmentFileRepo extends JpaRepository<AssignmentFile, Long> {

}
