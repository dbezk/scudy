package ua.scudy.server.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.constants.MailTemplate;
import ua.scudy.server.constants.RoleType;
import ua.scudy.server.data.StudentCreateData;
import ua.scudy.server.data.TeacherCreateData;
import ua.scudy.server.entity.user.ScudyRole;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.profile.StudentProfileStatistics;
import ua.scudy.server.entity.user.profile.TeacherDescription;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.entity.email.EmailConfirmationToken;
import ua.scudy.server.entity.user.profile.TeacherProfileStatistics;
import ua.scudy.server.mail.template.AccountConfirmationTemplate;
import ua.scudy.server.repository.user.*;
import ua.scudy.server.service.mail.MailSender;
import ua.scudy.server.service.token.TokensService;

import java.time.LocalDateTime;
import java.util.*;

@Service @Transactional
@RequiredArgsConstructor @Slf4j
public class AuthService implements UserDetailsService {

    @Value("${scudy.mail.confirmation.url}")
    private String mailLinkUrl;

    private final TeachersRepo teachersRepo;
    private final StudentsRepo studentsRepo;
    private final RoleRepo rolesRepo;
    private final TeacherDescriptionRepo teacherDescriptionRepo;
    private final TeacherProfileStatisticsRepo teacherProfileStatisticsRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokensService tokensService;
    private final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var teacher = teachersRepo.findByEmail(email);
        var student = studentsRepo.findByEmail(email);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(teacher != null) {
            teacher.getScudyRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName().name()));
            });
            return new User(teacher.getEmail(), teacher.getPassword(), authorities);
        }
        if(student != null) {
            student.getScudyRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName().name()));
            });
            return new User(student.getEmail(), student.getPassword(), authorities);
        }
        return null;
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public ScudyTeacher createTeacherAccount(TeacherCreateData teacherDtoRequest) {
        var role = rolesRepo.findByName(RoleType.TEACHER);
        Collection<ScudyRole> userRoles = new ArrayList<>(Collections.singletonList(role));

        var teacherDescription = new TeacherDescription(teacherDtoRequest.getProfileDescription(),
                teacherDtoRequest.getExperienceDescription());
        teacherDescriptionRepo.save(teacherDescription);


        var initialStat = new TeacherProfileStatistics();
        teacherProfileStatisticsRepo.save(initialStat);

        var encodedPassword = passwordEncoder.encode(teacherDtoRequest.getPassword());

        var newTeacher = new ScudyTeacher(teacherDtoRequest.getFirstName(), teacherDtoRequest.getLastName(),
                teacherDtoRequest.getEducation(), teacherDtoRequest.getEmail(), encodedPassword,
                userRoles, teacherDescription, initialStat);
        teachersRepo.save(newTeacher);

        var confirmationToken = new EmailConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                newTeacher.getEmail(), RoleType.TEACHER);
        tokensService.saveEmailConfirmationToken(confirmationToken);

        var mailTemplate = new AccountConfirmationTemplate(teacherDtoRequest.getFirstName(),
                teacherDtoRequest.getLastName(),
                String.format("%s?token=%s", mailLinkUrl, confirmationToken.getToken()));

        mailSender.send(teacherDtoRequest.getEmail(),
                MailTemplate.ACCOUNT_CONFIRMATION_MESSAGE_TEXT, mailTemplate);

        return newTeacher;
    }

    public ScudyStudent createStudentAccount(StudentCreateData studentDtoRequest) {
        var role = rolesRepo.findByName(RoleType.STUDENT);
        Collection<ScudyRole> userRoles = new ArrayList<>(Collections.singletonList(role));
        var initialStat = new StudentProfileStatistics();

        var encodedPassword = passwordEncoder.encode(studentDtoRequest.getPassword());
        var newStudent = new ScudyStudent(studentDtoRequest.getFirstName(), studentDtoRequest.getLastName(),
                studentDtoRequest.getEducation(), studentDtoRequest.getEmail(), encodedPassword,
                userRoles, initialStat);
        studentsRepo.save(newStudent);

        var confirmationToken = new EmailConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                newStudent.getEmail(), RoleType.STUDENT);
        tokensService.saveEmailConfirmationToken(confirmationToken);

        var mailTemplate = new AccountConfirmationTemplate(studentDtoRequest.getFirstName(),
                studentDtoRequest.getLastName(),
                String.format("%s?token=%s", mailLinkUrl, confirmationToken.getToken()));

        mailSender.send(studentDtoRequest.getEmail(),
                MailTemplate.ACCOUNT_CONFIRMATION_MESSAGE_TEXT, mailTemplate);

        return newStudent;
    }
}
