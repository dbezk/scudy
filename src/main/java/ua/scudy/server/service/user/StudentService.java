package ua.scudy.server.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.compiler.SolutionCodeCompiler;
import ua.scudy.server.constants.SolutionResult;
import ua.scudy.server.data.StudentCreateData;
import ua.scudy.server.data.assignment.AssignmentFullInfo;
import ua.scudy.server.data.assignment.solution.*;
import ua.scudy.server.data.user.UpdateStudentPersonalData;
import ua.scudy.server.data.view.TeacherView;
import ua.scudy.server.entity.assignment.Assignment;
import ua.scudy.server.entity.assignment.AssignmentRating;
import ua.scudy.server.entity.assignment.AssignmentSolution;
import ua.scudy.server.entity.user.Follow;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.repository.assignment.AssignmentRatingRepo;
import ua.scudy.server.repository.assignment.AssignmentRepo;
import ua.scudy.server.repository.user.FollowsRepo;
import ua.scudy.server.repository.user.StudentsRepo;
import ua.scudy.server.repository.user.TeachersRepo;
import ua.scudy.server.service.assignment.AssignmentService;
import ua.scudy.server.service.auth.AuthService;
import ua.scudy.server.service.file.FileConverter;
import ua.scudy.server.service.file.FileStorageService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentsRepo studentsRepo;
    private final TeachersRepo teachersRepo;
    private final AssignmentRepo assignmentsRepo;
    private final FileStorageService fileStorageService;
    private final FileConverter fileConverter;
    private final SolutionCodeCompiler solutionCodeCompiler;
    private final AssignmentService assignmentService;
    private final FollowsRepo followsRepo;
    private final AssignmentRatingRepo ratingRepo;
    private final PasswordEncoder passwordEncoder;

    public ScudyStudent getStudentInfo() {
        return studentsRepo.findByEmail(AuthService.getAuthentication().getName());
    }

    public void updateStudentInfo(UpdateStudentPersonalData personalData) {
        var student = getStudentInfo();
        student.setFirstName(personalData.getFirstName() != null ?
                personalData.getFirstName() : student.getFirstName());
        student.setLastName(personalData.getLastName() != null ?
                personalData.getLastName() : student.getLastName());
        student.setEducation(personalData.getEducation() != null ?
                personalData.getEducation() : student.getEducation());
        student.setPassword(personalData.getPassword() != null ?
                passwordEncoder.encode(personalData.getPassword()) : student.getPassword());
        studentsRepo.save(student);
    }

    public List<ScudyTeacher> fetchAllTeachers() {
        return teachersRepo.findAll();
    }

    public TeacherView fetchTeacherById(Long id) {
        var teacher = teachersRepo.findById(id).orElseThrow(() -> {
            throw new UsernameNotFoundException("teacher not found!");
        });
        var student = getStudentInfo();
        boolean follow = followsRepo.findByStudentAndTeacher(student, teacher) != null;
        return TeacherView.builder()
                .teacher(teacher)
                .isFollow(follow)
                .build();
    }

    public void followOnTeacher(Long id) {
        var studentEmail = AuthService.getAuthentication().getName();
        var student = studentsRepo.findByEmail(studentEmail);
        var teacher = teachersRepo.findById(id)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("teacher not found");
                });
        var newFollow = new Follow(teacher, student);
        followsRepo.save(newFollow);
        teachersRepo.incrementCountOfTeacherFollowers(teacher.getEmail());
        studentsRepo.save(student);
        student.getFollowList().add(newFollow);
        studentsRepo.incrementCountOfStudentFollows(studentEmail);
    }

    public void unfollowOnTeacher(Long id) {
        var studentEmail = AuthService.getAuthentication().getName();
        var student = getStudentInfo();
        var teacher = teachersRepo.findById(id)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("teacher not found");
                });
        var followList = student.getFollowList().stream().filter(follow -> follow.getTeacher().equals(teacher))
                .toList();
        log.info("follow list = {}", followList);
        followsRepo.deleteAll(followList);
        student.getFollowList().removeAll(followList);

        teachersRepo.decrementCountOfTeacherFollowers(teacher.getEmail());
        studentsRepo.decrementCountOfStudentFollows(studentEmail);
    }

    public List<Assignment> fetchAllAssignments() {
        return assignmentsRepo.findAll();
    }

    public AssignmentFullInfo fetchAssignment(Long id) {
        var assignment = assignmentsRepo.findById(id).orElseThrow(() -> {
           throw new UsernameNotFoundException("assignment not found!!!!");
        });
        var assignmentFile = fileStorageService.getAssignmentFile(id);
        var assignmentData = fileConverter.convertAssignmentFileToObject(assignmentFile);
        var studentEmail = AuthService.getAuthentication().getName();
        var student = studentsRepo.findByEmail(studentEmail);

        var assignmentFullInfo = new AssignmentFullInfo();

        for(var solution : assignment.getAssignmentSolutions()) {
            if(solution.getSenderOfSolution().getEmail().equals(studentEmail)) {
                if(solution.getSolutionResult() == SolutionResult.DONE) {
                    assignmentFullInfo.setDone(true);
                } else {
                    assignmentFullInfo.setUserFailedSolutionsCount(assignmentFullInfo.getFailedSolutionsCount() + 1);
                }
            }
            if (solution.getSolutionResult() == SolutionResult.DONE) {
                assignmentFullInfo.setDoneSolutionsCount(assignmentFullInfo.getDoneSolutionsCount() + 1);
            } else {
                assignmentFullInfo.setFailedSolutionsCount(assignmentFullInfo.getFailedSolutionsCount() + 1);
            }
        }
        boolean isRated = assignment.getAssignmentRatings().stream().anyMatch(item -> item.getSenderOfRating().equals(getStudentInfo()));
        double rating = assignmentService.getAssignmentRating(id);
        assignmentFullInfo.setRating(rating);
        assignmentFullInfo.setRated(isRated);
        assignmentFullInfo.setAssignmentData(assignmentData);
        boolean follow = student.getFollowList().stream().anyMatch(f -> (f.getTeacher() == assignment.getTeacher()));
        assignmentFullInfo.setStudentFollow(follow);
        assignmentFullInfo.setScudyTeacher(assignment.getTeacher());
        return assignmentFullInfo;
    }

    public SolutionType sendSolution(AssignmentSolutionData solutionData)
            throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // TODO: check assignment exists
        var newSolutionFile = fileStorageService.createSolutionFile(solutionData.getCode());
        // for convert
        var assignmentFile = fileStorageService.getAssignmentFile(solutionData.getAssignmentId());
        // end
        var assignmentData = fileConverter.convertAssignmentFileToObject(assignmentFile);

        var studentEmail = AuthService.getAuthentication().getName();

        SolutionType solutionResult =
                solutionCodeCompiler.compileAssignmentSolution(solutionData.getCode(), newSolutionFile, assignmentData);

        if(solutionResult instanceof DoneSolution) {
            assignmentService.createSolution(solutionData, SolutionResult.DONE);
            studentsRepo.incrementCountOfStudentDoneSolutions(studentEmail);
        }
        if(solutionResult instanceof FailedSolution || solutionResult instanceof FailedSyntaxSolution) {
            assignmentService.createSolution(solutionData, SolutionResult.FAIL);
            studentsRepo.incrementCountOfStudentFailedSolutions(studentEmail);
        }


        return solutionResult;
    }

    public void setAssignmentRating(Long id, int rating) {
        var assignment = assignmentsRepo.findById(id).orElseThrow(() -> {
            throw new UsernameNotFoundException("Assignment not found");
        });
        var newRating = new AssignmentRating(getStudentInfo(), assignment, rating);
        ratingRepo.save(newRating);
        assignment.getAssignmentRatings().add(newRating);
        assignmentsRepo.save(assignment);
    }

    public List<ScudyTeacher> getFollowings() {
        return getStudentInfo().getFollowList().stream()
                .map(Follow::getTeacher).toList();
    }

    public List<Assignment> getStudentDoneAssignments() {
        return getStudentInfo().getStudentSolutions().stream()
                .filter(solution -> solution.getSolutionResult().equals(SolutionResult.DONE))
                .map(AssignmentSolution::getAssignment).toList();
    }
}
