package ua.scudy.server.service.mail;

import ua.scudy.server.constants.MailTemplate;

public interface MailSender {

     void send(String to, MailTemplate mailSubject, ua.scudy.server.mail.template.MailTemplate template);

}
