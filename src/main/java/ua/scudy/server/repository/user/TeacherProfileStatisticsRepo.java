package ua.scudy.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.user.profile.TeacherProfileStatistics;

@Repository
public interface TeacherProfileStatisticsRepo extends JpaRepository<TeacherProfileStatistics, Long> {



}
