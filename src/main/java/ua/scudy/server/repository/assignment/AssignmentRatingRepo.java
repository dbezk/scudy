package ua.scudy.server.repository.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.scudy.server.entity.assignment.AssignmentRating;

public interface AssignmentRatingRepo extends JpaRepository<AssignmentRating, Long> {
}
