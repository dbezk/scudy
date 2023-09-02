package ua.scudy.server.api.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.scudy.server.data.StudentCreateData;
import ua.scudy.server.data.TeacherCreateData;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.service.auth.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;

//    @PostMapping(value = {"/student/create", "/teacher/create"})
//    protected ResponseEntity<ScudyTeacher> createTeacher(HttpServletRequest request,
//                                                         @RequestBody TeacherCreateRequest teacherInfo) {
//        var requestUrl = request.getRequestURI();
//        var requestUri = URI.create(requestUrl);
//        log.info("RequestUrl = {}, RequestUri = {}", requestUrl, requestUri);
//        return ResponseEntity.created(requestUri).body(authService.createTeacherAccount(teacherInfo, userRole));
//    }


    @PostMapping(value = "/student/create")
    protected ResponseEntity<ScudyStudent> createStudent(HttpServletRequest request,
                                                         @RequestBody StudentCreateData studentInfo) {
        String requestUrl = request.getRequestURI();
        URI requestUri = URI.create(requestUrl);
        return ResponseEntity.created(requestUri).body(authService.createStudentAccount(studentInfo));
    }

    @PostMapping(value = "/teacher/create")
    protected ResponseEntity<ScudyTeacher> createTeacher(HttpServletRequest request,
                                                         @RequestBody TeacherCreateData teacherData) {
        String requestUrl = request.getRequestURI();
        URI requestUri = URI.create(requestUrl);
        return ResponseEntity.created(requestUri).body(authService.createTeacherAccount(teacherData));
    }
}
