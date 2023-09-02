package ua.scudy.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static java.util.Arrays.stream;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {

//    private final StudentService studentService;
//
//    @GetMapping("/students")
//    public ResponseEntity<List<ScudyTeacher>> getStudents() {
//        return ResponseEntity.ok().body(studentService.getStudents());
//    }
//
//    @PostMapping("/student/save")
//    public ResponseEntity<ScudyTeacher> saveStudent(@RequestBody ScudyTeacher scudyTeacher) {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/student/save").toUriString());
//        return ResponseEntity.created(uri).body(studentService.saveStudent(scudyTeacher));
//    }
//
//    @PostMapping("/role/save")
//    public ResponseEntity<ScudyRole> saveStudent(@RequestBody ScudyRole scudyRole) {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
//        return ResponseEntity.created(uri).body(studentService.saveRole(scudyRole));
//    }
//
//    @GetMapping("/role/save")
//    public ResponseEntity<?> saveStudent(@RequestBody RoleToUserForm form) {
//        studentService.addRoleToStudent(form.getEmail(), RoleConstants.valueOf(form.getRoleName()));
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/token/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256("secret_cat".getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
//                String email = decodedJWT.getSubject();
//                ScudyTeacher scudyTeacher = studentService.getStudent(email);
//                String access_token = JWT.create()
//                        .withSubject(scudyTeacher.getEmail())
//                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
//                        .withIssuer(request.getRequestURI().toString())
//                        .withClaim("roles", scudyTeacher.getScudyRoles().stream().map(thisRole -> {
//                            return thisRole.getName().name();
//                        }).collect(Collectors.toList()))
//                        .sign(algorithm);
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            } catch (Exception e) {
//                response.setHeader("error", e.getMessage());
//                response.setStatus(FORBIDDEN.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", e.getMessage());
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        } else {
//            throw new RuntimeException("Refresh token missing");
//        }
//    }

}

