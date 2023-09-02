package ua.scudy.server.repository.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.assignment.AssignmentSolution;

@Repository
public interface AssignmentSolutionRepo extends JpaRepository<AssignmentSolution, Long> {

}
