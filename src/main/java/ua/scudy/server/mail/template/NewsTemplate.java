package ua.scudy.server.mail.template;

public class NewsTemplate extends MailTemplate{

    private final String firstName;
    private final String lastName;

    private final String teacherFirstName;
    private final String teacherLastName;

    public NewsTemplate(String firstName,
                        String lastName,
                        String teacherFirstName,
                        String teacherLastName) {
        super(ua.scudy.server.constants.MailTemplate.ACCOUNT_NEWS_MESSAGE);
        this.firstName = firstName;
        this.lastName = lastName;
        this.teacherFirstName = teacherFirstName;
        this.teacherLastName = teacherLastName;
    }

    @Override
    public String buildTemplate(String... args) {
        return super.buildTemplate(firstName, lastName,
                teacherFirstName, teacherLastName);
    }
}
