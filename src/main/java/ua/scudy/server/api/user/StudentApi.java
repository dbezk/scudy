package ua.scudy.server.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.scudy.server.data.StudentCreateData;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.data.assignment.AssignmentFullInfo;
import ua.scudy.server.data.assignment.AssignmentRatingData;
import ua.scudy.server.data.assignment.solution.AssignmentSolutionData;
import ua.scudy.server.data.assignment.solution.DoneSolution;
import ua.scudy.server.data.assignment.solution.SolutionType;
import ua.scudy.server.data.user.IdData;
import ua.scudy.server.data.user.UpdateStudentPersonalData;
import ua.scudy.server.data.view.TeacherView;
import ua.scudy.server.entity.assignment.Assignment;
import ua.scudy.server.entity.user.Follow;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.service.user.StudentService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentApi {

    private final StudentService studentService;


    /* Student */

    @GetMapping
    public ResponseEntity<ScudyStudent> getStudentInfo() {
        return ResponseEntity.ok().body(studentService.getStudentInfo());
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateStudentInfo(
            @RequestBody UpdateStudentPersonalData personalData) {
        studentService.updateStudentInfo(personalData);
        return ResponseEntity.ok().build();
    }


    /* Teachers */

    @GetMapping(value = "/teachers", produces = APPLICATION_JSON_VALUE)
    public List<ScudyTeacher> fetchAllTeachers() {
        return studentService.fetchAllTeachers();
    }

    @GetMapping(value = "/teacher/{id}", produces = APPLICATION_JSON_VALUE)
    public TeacherView fetchTeacherById(
            @PathVariable(value="id") Long id) {
        return studentService.fetchTeacherById(id);
    }

    @PostMapping(value = "/teachers/follow")
    public ResponseEntity<?> followOnTeacher(@RequestBody IdData idData) {
        studentService.followOnTeacher(idData.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/teachers/unfollow/{id}")
    public ResponseEntity<?> unfollowOnTeacher(
            @PathVariable(value="id") Long id
    ) {
        studentService.unfollowOnTeacher(id);
        return ResponseEntity.ok().build();
    }


    /* Followings */

    @GetMapping(value = "/followings")
    public List<ScudyTeacher> getStudentFollows() {
        return studentService.getFollowings();
    }


    /* Assignments */

    @GetMapping(value = "/assignments")
    public List<Assignment> fetchAllAssignments() {
        return studentService.fetchAllAssignments();
    }

    @GetMapping(value = "/assignments/my")
    public List<Assignment> fetchAllDoneAssignments() {
        return studentService.getStudentDoneAssignments();
    }

    @GetMapping(value = "/assignment/{id}")
    public AssignmentFullInfo fetchAssignment(
            @PathVariable(value="id") Long id) {
        return studentService.fetchAssignment(id);
    }

    @PostMapping(value = "/assignment/solution")
    public ResponseEntity<SolutionType> sendAssignmentSolution(@RequestBody AssignmentSolutionData solutionData) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // TODO: check assignment exists
        var solutionType = studentService.sendSolution(solutionData);
        if(solutionType instanceof DoneSolution) {
            return ResponseEntity.ok().body(solutionType);
        }
        return ResponseEntity.badRequest().body(solutionType);
    }

    @PostMapping(value = "/assignment/rating")
    public ResponseEntity<SolutionType> sendAssignmentSolution(
            @RequestBody AssignmentRatingData ratingData) {
        studentService.setAssignmentRating(ratingData.getId(), ratingData.getRating());
        return ResponseEntity.ok().build();
    }

}
