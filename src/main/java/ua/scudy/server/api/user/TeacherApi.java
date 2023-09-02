package ua.scudy.server.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.data.user.IdData;
import ua.scudy.server.data.user.UpdateTeacherPersonalData;
import ua.scudy.server.data.view.TeacherAssignmentView;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.service.assignment.AssignmentService;
import ua.scudy.server.service.user.TeacherService;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherApi {

    private final TeacherService teacherService;
    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<ScudyTeacher> getTeacherInfo() {
        return ResponseEntity.ok().body(teacherService.getTeacherInfo());
    }


    @PutMapping(value = "/update")
    public ResponseEntity<?> updateTeacherInfo(
            @RequestBody UpdateTeacherPersonalData teacherData) {
        teacherService.updateTeacherInfo(teacherData);
        return ResponseEntity.ok().build();
    }

    /* Followings */

    @GetMapping(value = "/followers")
    public List<ScudyStudent> getStudentFollows() {
        return teacherService.getFollowersList();
    }


    @GetMapping(value = "/assignment/{id}")
    public TeacherAssignmentView fetchAssignment(
            @PathVariable(value="id") Long id) {
        return teacherService.getAssignmentById(id);
    }

    @PostMapping(value = "/assignment/create", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentData assignmentData) throws JAXBException, IOException {
        assignmentService.createAssignment(assignmentData);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/assignment/delete")
    public ResponseEntity<?> deleteAssignment(@RequestBody IdData idData) {
        assignmentService.deleteAssignment(idData.getId());
        return ResponseEntity.ok().build();
    }

}
