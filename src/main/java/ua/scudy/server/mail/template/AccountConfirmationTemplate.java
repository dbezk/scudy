package ua.scudy.server.mail.template;

import ua.scudy.server.constants.MailTemplate;


public class AccountConfirmationTemplate extends ua.scudy.server.mail.template.MailTemplate {

    private final String firstName;
    private final String lastName;
    private final String link;

    public AccountConfirmationTemplate(String firstName, String lastName, String link) {
        super(MailTemplate.ACCOUNT_CONFIRMATION_MESSAGE);
        this.firstName = firstName;
        this.lastName = lastName;
        this.link = link;
    }

    @Override
    public String buildTemplate(String... args) {
        return super.buildTemplate(firstName, lastName, link);
    }
}
