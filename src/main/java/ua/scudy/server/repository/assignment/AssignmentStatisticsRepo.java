package ua.scudy.server.repository.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.assignment.AssignmentStatistics;

@Repository
public interface AssignmentStatisticsRepo extends JpaRepository<AssignmentStatistics, Long> {

    @Modifying
    @Query(value = "UPDATE AssignmentStatistics ast SET ast.doneSolutionsCount = ast.doneSolutionsCount+1 WHERE ast.id in " +
            "( SELECT at.assignmentStatistics FROM Assignment at WHERE at.id = :assignmentId )")
    void incrementCountOfDoneSolutions(@Param("assignmentId") Long id);

    @Modifying
    @Query(value = "UPDATE AssignmentStatistics ast SET ast.failedSolutionsCount = ast.failedSolutionsCount+1 WHERE ast.id in " +
            "( SELECT at.assignmentStatistics FROM Assignment at WHERE at.id = :assignmentId )")
    void incrementCountOfFailedSolutions(@Param("assignmentId") Long id);

}
