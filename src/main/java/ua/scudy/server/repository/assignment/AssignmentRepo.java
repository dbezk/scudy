package ua.scudy.server.repository.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.assignment.Assignment;
import ua.scudy.server.entity.user.ScudyTeacher;

import java.util.List;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long> {

    @Modifying
    @Query("select a from Assignment a ORDER BY a.createdAt DESC")
    List<Assignment> findAllTAssignmentsByCreatedDateNewest();

    @Modifying
    @Query("SELECT a from Assignment a ORDER BY a.createdAt ASC")
    List<Assignment> findAllTAssignmentsByCreatedDateOldest();

    @Modifying
    @Query("SELECT a FROM Assignment a " +
            "LEFT JOIN AssignmentRating ar ON " +
            "ar.assignment = a " +
            "GROUP BY a.id " +
            "ORDER BY avg(ar.solutionRating) DESC")
    List<Assignment> findAllTAssignmentsByRatingDesc();

    @Modifying
    @Query("SELECT a FROM Assignment a " +
            "LEFT JOIN AssignmentSolution asl ON " +
            "asl.assignment = a " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(asl) DESC")
    List<Assignment> findAllTAssignmentsBySolutionsDesc();

}
