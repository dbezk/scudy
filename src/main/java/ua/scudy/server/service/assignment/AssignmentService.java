package ua.scudy.server.service.assignment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.constants.SolutionResult;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.data.assignment.solution.AssignmentSolutionData;
import ua.scudy.server.entity.assignment.Assignment;
import ua.scudy.server.entity.assignment.AssignmentFile;
import ua.scudy.server.entity.assignment.AssignmentRating;
import ua.scudy.server.entity.assignment.AssignmentSolution;
import ua.scudy.server.entity.user.Follow;
import ua.scudy.server.repository.assignment.AssignmentFileRepo;
import ua.scudy.server.repository.assignment.AssignmentRepo;
import ua.scudy.server.repository.assignment.AssignmentSolutionRepo;
import ua.scudy.server.repository.assignment.AssignmentStatisticsRepo;
import ua.scudy.server.repository.user.FollowsRepo;
import ua.scudy.server.repository.user.StudentsRepo;
import ua.scudy.server.repository.user.TeachersRepo;
import ua.scudy.server.service.auth.AuthService;
import ua.scudy.server.service.file.FileStorageService;
import ua.scudy.server.service.mail.MailSender;
import ua.scudy.server.service.mail.MailService;
import ua.scudy.server.service.user.TeacherService;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@Getter
@Setter
public class AssignmentService {

    private final FileStorageService fileStorageService;
    private final AssignmentFileRepo assignmentFileRepo;
    private final AssignmentRepo assignmentRepo;
    private final StudentsRepo studentsRepo;
    private final TeachersRepo teachersRepo;
    private final AssignmentSolutionRepo solutionRepo;
    private final AssignmentStatisticsRepo statisticsRepo;
    private final MailService mailService;
    private final FollowsRepo followsRepo;

    public void createAssignment(AssignmentData assignmentData) throws JAXBException, IOException {
        var assignmentFile = new AssignmentFile(String.format("%s.xml", UUID.randomUUID()));
        fileStorageService.createAssignmentFile(assignmentData, assignmentFile.getName());
        assignmentFileRepo.save(assignmentFile);
        var assignment = new Assignment(assignmentData.getAssignmentTitle(),
                assignmentData.getAssignmentDescription(), assignmentFile);
        var teacher = teachersRepo.findByEmail(AuthService.getAuthentication().getName());
        teacher.getAssignmentList().add(assignment);
        assignment.setTeacher(teacher);
        assignmentRepo.save(assignment);
        teachersRepo.save(teacher);
        teachersRepo.incrementCountOfTeacherAssignments(AuthService.getAuthentication().getName());
        var students = followsRepo.findAllByTeacher(teacher).stream().map(Follow::getStudent).toList();
        mailService.assignmentMailing(teacher, students);
    }

    public void createSolution(AssignmentSolutionData solutionData, SolutionResult solutionResult) {
        if(solutionResult == SolutionResult.DONE) {
            statisticsRepo.incrementCountOfDoneSolutions(solutionData.getAssignmentId());
        }
        if(solutionResult == SolutionResult.FAIL) {
            statisticsRepo.incrementCountOfFailedSolutions(solutionData.getAssignmentId());
        }
        var student = studentsRepo.findByEmail(AuthService.getAuthentication().getName());
        var assignment = assignmentRepo.findById(solutionData.getAssignmentId()).orElseThrow(() -> {
            throw new UsernameNotFoundException("assignment not found");
        });
        var newSolution = solutionRepo.save(
                new AssignmentSolution(student, assignment, solutionResult)
        );
        assignment.getAssignmentSolutions().add(newSolution);
        student.getStudentSolutions().add(newSolution);
        studentsRepo.save(student);
    }

    public void deleteAssignment(Long id) {
        assignmentRepo.deleteById(id);
    }

    public double getAssignmentRating(Long id) {
        Assignment assignment = assignmentRepo.findById(id).orElseThrow(() -> {
            throw new UsernameNotFoundException("Assignment not found!");
        });
        double temp = assignment.getAssignmentRatings().stream().mapToInt(AssignmentRating::getSolutionRating)
                .summaryStatistics().getAverage();
        return temp;
    }

}
