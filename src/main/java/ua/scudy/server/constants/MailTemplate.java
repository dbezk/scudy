package ua.scudy.server.constants;

public enum MailTemplate {

    ACCOUNT_CONFIRMATION_MESSAGE("confirm_email_template.html"),
    ACCOUNT_CONFIRMATION_MESSAGE_TEXT("Scudy: confirm your email"),

    ACCOUNT_NEWS_MESSAGE("news_email_template.html"),
    ACCOUNT_NEWS_MESSAGE_TEXT("Scudy: news");

    private final String value;

    MailTemplate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
