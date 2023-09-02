package ua.scudy.server.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.repository.user.StudentsRepo;
import ua.scudy.server.repository.user.TeachersRepo;

import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor @Slf4j
public class UserControlService {

    private final TeachersRepo teachersRepo;
    private final StudentsRepo studentsRepo;

    public void setTeacherAccountEnable(String email) {
        var teacher = Optional.ofNullable(teachersRepo.findByEmail(email))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Teacher not found in the database");
                });
        teacher.setActivated(true);
        teachersRepo.save(teacher);
    }

    public void setStudentAccountEnable(String email) {
        var student= Optional.ofNullable(studentsRepo.findByEmail(email))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Student not found in the database");
                });
        student.setActivated(true);
        studentsRepo.save(student);
    }

}
