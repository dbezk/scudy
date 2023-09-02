package ua.scudy.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.user.ScudyTeacher;

import java.util.List;

@Repository
public interface TeachersRepo extends JpaRepository<ScudyTeacher, Long> {

    ScudyTeacher findByEmail(String email);

    @Modifying
    @Query(value = "SELECT st FROM ScudyTeacher st ORDER BY function('rand')")
    List<ScudyTeacher> findAllAndOrderByRandomSort();

    @Modifying
    @Query(value = "UPDATE TeacherProfileStatistics tps SET tps.assignmentsCount = tps.assignmentsCount+1  WHERE tps.id in " +
            "( SELECT st.teacherProfileStatistics FROM ScudyTeacher st WHERE st.email = :teacherEmail )  ")
    void incrementCountOfTeacherAssignments(@Param("teacherEmail") String email);

    @Modifying
    @Query(value = "UPDATE TeacherProfileStatistics tps SET tps.assignmentsCount = tps.assignmentsCount-1  WHERE tps.id in " +
            "( SELECT st.teacherProfileStatistics FROM ScudyTeacher st WHERE st.email = :teacherEmail )  ")
    void decrementCountOfTeacherAssignments(@Param("teacherEmail") String email);

    @Modifying
    @Query(value = "UPDATE TeacherProfileStatistics tps SET tps.followersCount = tps.followersCount+1  WHERE tps.id in " +
            "( SELECT st.teacherProfileStatistics FROM ScudyTeacher st WHERE st.email = :teacherEmail )  ")
    void incrementCountOfTeacherFollowers(@Param("teacherEmail") String email);

    @Modifying
    @Query(value = "UPDATE TeacherProfileStatistics tps SET tps.followersCount = tps.followersCount-1  WHERE tps.id in " +
            "( SELECT st.teacherProfileStatistics FROM ScudyTeacher st WHERE st.email = :teacherEmail )  ")
    void decrementCountOfTeacherFollowers(@Param("teacherEmail") String email);

    @Modifying
    @Query("SELECT st FROM ScudyTeacher st " +
            "LEFT JOIN st.teacherProfileStatistics tsp " +
            "ORDER BY tsp.assignmentsCount DESC")
    List<ScudyTeacher> findAllTeachersByAssignmentsCountDesc();

    @Modifying
    @Query("SELECT st FROM ScudyTeacher st " +
            "LEFT JOIN st.teacherProfileStatistics tsp " +
            "ORDER BY tsp.followersCount DESC")
    List<ScudyTeacher> findAllTeachersByFollowersCountDesc();

}
