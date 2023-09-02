package ua.scudy.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.user.Follow;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;

import java.util.List;

@Repository
public interface FollowsRepo extends JpaRepository<Follow, Long> {

//    @Modifying
//    @Query("DELETE FROM Follow f WHERE f.student = :sId AND f.teacher = :tId")
//    void unfollowOnTeacher(@Param("teacherId") Long tId, @Param("studentId") Long sId);

    Follow findByStudentAndTeacher(ScudyStudent ss, ScudyTeacher st);

    List<Follow> findAllByTeacher(ScudyTeacher st);
    List<Follow> findAllByStudent(ScudyStudent ss);

}
