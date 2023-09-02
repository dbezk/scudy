package ua.scudy.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.user.profile.TeacherDescription;

@Repository
public interface TeacherDescriptionRepo extends JpaRepository<TeacherDescription, Long> {
}
