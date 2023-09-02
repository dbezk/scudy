package ua.scudy.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;

@Repository
public interface StudentsRepo extends JpaRepository<ScudyStudent, Long> {

    ScudyStudent findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE StudentProfileStatistics sps SET sps.followsCount = sps.followsCount+1  WHERE sps.id in " +
            "( SELECT ss.studentProfileStatistics FROM ScudyStudent ss WHERE ss.email = :studentEmail )  ")
    void incrementCountOfStudentFollows(@Param("studentEmail") String email);

    @Modifying
    @Query(value = "UPDATE StudentProfileStatistics sps SET sps.followsCount = sps.followsCount-1  WHERE sps.id in " +
            "( SELECT ss.studentProfileStatistics FROM ScudyStudent ss WHERE ss.email = :studentEmail )  ")
    void decrementCountOfStudentFollows(@Param("studentEmail") String email);

    @Modifying
    @Query(value = "UPDATE StudentProfileStatistics sps SET sps.doneSolutions = sps.doneSolutions+1  WHERE sps.id in " +
            "( SELECT ss.studentProfileStatistics FROM ScudyStudent ss WHERE ss.email = :studentEmail )  ")
    void incrementCountOfStudentDoneSolutions(@Param("studentEmail") String email);

    @Modifying
    @Query(value = "UPDATE StudentProfileStatistics sps SET sps.failedSolutions = sps.failedSolutions+1  WHERE sps.id in " +
            "( SELECT ss.studentProfileStatistics FROM ScudyStudent ss WHERE ss.email = :studentEmail )  ")
    void incrementCountOfStudentFailedSolutions(@Param("studentEmail") String email);

}