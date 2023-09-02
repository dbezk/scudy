package ua.scudy.server.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua.scudy.server.constants.MailTemplate;
import ua.scudy.server.entity.user.ScudyStudent;
import ua.scudy.server.entity.user.ScudyTeacher;
import ua.scudy.server.mail.template.NewsTemplate;

import javax.mail.MessagingException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService implements MailSender {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, MailTemplate mailSubject, ua.scudy.server.mail.template.MailTemplate template) {
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            messageHelper.setFrom("Scudy<senderacclok@gmail.com>");
            messageHelper.setTo(to);
            messageHelper.setSubject(mailSubject.getValue());
            messageHelper.setText(template.buildTemplate(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send message: ", e);
            throw new IllegalStateException("Failed to send message");
        }
    }

    public void assignmentMailing(ScudyTeacher teacher, List<ScudyStudent> students) {
        log.info("received follows list = {}, from teacher = {}", students, teacher);
        for(var student : students) {
            var mailTemplate = new NewsTemplate(student.getFirstName(),
                    student.getLastName(), teacher.getFirstName(), teacher.getLastName());
            send(student.getEmail(),
                    MailTemplate.ACCOUNT_NEWS_MESSAGE_TEXT, mailTemplate);
            log.info("send email to = {}", student.getEmail());
        }
    }

}
