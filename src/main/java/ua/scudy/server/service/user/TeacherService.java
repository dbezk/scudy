package ua.scudy.server.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.data.StudentCreateData;
import ua.scudy.server.data.TeacherCreateData;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.data.assignment.AssignmentFullInfo;
import ua.scudy.server.data.user.UpdateTeacherPersonalData;
import ua.scudy.server.data.view.TeacherAssignmentView;
import ua.scudy.server.entity.user.Follow;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.repository.assignment.AssignmentRepo;
import ua.scudy.server.repository.user.FollowsRepo;
import ua.scudy.server.repository.user.TeacherProfileStatisticsRepo;
import ua.scudy.server.repository.user.TeachersRepo;
import ua.scudy.server.service.assignment.AssignmentService;
import ua.scudy.server.service.auth.AuthService;
import ua.scudy.server.service.file.FileConverter;
import ua.scudy.server.service.file.FileStorageService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final AssignmentService assignmentService;
    private final TeachersRepo teachersRepo;
    private final AssignmentRepo assignmentRepo;
    private final PasswordEncoder passwordEncoder;
    private final FileConverter fileConverter;
    private final FileStorageService fileStorageService;
    private final FollowsRepo followsRepo;

    public ScudyTeacher getTeacherInfo() {
        return teachersRepo.findByEmail(AuthService.getAuthentication().getName());
    }

    public TeacherAssignmentView getAssignmentById(Long id) {
        var assignment = assignmentRepo.findById(id).orElseThrow(() -> {
            throw new UsernameNotFoundException("Assignment not found!");
        });
        var assignmentFile = fileStorageService.getAssignmentFile(id);
        var assignmentData = fileConverter.convertAssignmentFileToObject(assignmentFile);
        AssignmentFullInfo fullInfo = new AssignmentFullInfo();
        fullInfo.setAssignmentData(assignmentData);
        fullInfo.setDoneSolutionsCount(assignment.getAssignmentStatistics().getDoneSolutionsCount());
        fullInfo.setFailedSolutionsCount(assignment.getAssignmentStatistics().getFailedSolutionsCount());
        fullInfo.setRating(assignmentService.getAssignmentRating(id));
        return TeacherAssignmentView.builder()
                .assignmentData(assignmentData)
                .assignmentStatistics(assignment.getAssignmentStatistics())
                .rating(assignmentService.getAssignmentRating(id))
                .build();
    }

    public void updateTeacherInfo(UpdateTeacherPersonalData teacherData) {
        var teacher = getTeacherInfo();
        teacher.setFirstName(teacherData.getFirstName() != null ?
                teacherData.getFirstName() : teacher.getFirstName());
        teacher.setLastName(teacherData.getLastName() != null ?
                teacherData.getLastName() : teacher.getLastName());
        teacher.setEducation(teacherData.getEducation() != null ?
                teacherData.getEducation() : teacher.getEducation());
        teacher.setPassword(teacherData.getPassword() != null ?
                passwordEncoder.encode(teacherData.getPassword()) : teacher.getPassword());
        if(teacherData.getExperienceDescription() != null) {
            teacher.getTeacherDescription().setExperienceDescription(teacherData.getExperienceDescription());
        }
        if(teacherData.getProfileDescription() != null) {
            teacher.getTeacherDescription().setProfileDescription(teacherData.getProfileDescription());
        }
        teachersRepo.save(teacher);
    }

    public List<ScudyStudent> getFollowersList() {
        return followsRepo.findAllByTeacher(getTeacherInfo())
                .stream().map(Follow::getStudent).toList();
    }
}
