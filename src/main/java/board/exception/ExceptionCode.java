package board.exception;

import lombok.Getter;

public enum ExceptionCode {
    EMAIL_EXISTS(404, "Email exists"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    PASSWORD_NOT_MATCH(401, "Password not match"),
    LOGIN_NEEDED(401, "Login needed"),

    ARTICLE_NOT_FOUND(404, "Article not found"),
    ARTICLE_MEMBER_NOT_MATCH(401, "Article is not written by member");

    @Getter
    int code;

    @Getter
    String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
