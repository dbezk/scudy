package ua.scudy.server.service.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.constants.RoleType;
import ua.scudy.server.entity.email.EmailConfirmationToken;
import ua.scudy.server.repository.email.ConfirmationTokenRepo;
import ua.scudy.server.service.mail.MailSender;
import ua.scudy.server.service.user.UserControlService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor @Slf4j
public class TokensService {

    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final UserControlService userControlService;
    private final MailSender mailSender;

    public void saveEmailConfirmationToken(EmailConfirmationToken token) {
        confirmationTokenRepo.save(token);
    }

    public String confirmEmailConfirmationToken(String token) {
        var thisToken = Optional.ofNullable(confirmationTokenRepo.findByToken(token))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Token not found.");
                });
        if(thisToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime tokenExpires = thisToken.getExpiresAt();
        if(tokenExpires.isBefore(LocalDateTime.now())) {
            throw  new IllegalStateException("Token expired");
        }

        thisToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepo.save(thisToken);
        if(thisToken.getUserRole() == RoleType.TEACHER) {
            userControlService.setTeacherAccountEnable(thisToken.getEmail());
        }
        if(thisToken.getUserRole() == RoleType.STUDENT) {
            userControlService.setStudentAccountEnable(thisToken.getEmail());
        }
        return "confirmed";
    }

}
