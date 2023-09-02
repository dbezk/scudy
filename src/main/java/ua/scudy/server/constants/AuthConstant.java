package ua.scudy.server.constants;

public enum AuthConstant {


    BEARER_HEADER("Bearer "),

    USER_ACCESS_TOKEN_TEXT("access_token"),
    USER_REFRESH_TOKEN_TEXT("refresh_token");

    public final String value;
    AuthConstant(String value) {
        this.value = value;
    }
}