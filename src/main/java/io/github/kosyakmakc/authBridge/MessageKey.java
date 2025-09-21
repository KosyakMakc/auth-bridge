package io.github.kosyakmakc.authBridge;

public record MessageKey(String key) {
    public static final MessageKey LOGIN_FROM_MINECRAFT = new MessageKey("login_from_minecraft");
    public static final MessageKey COMMITED_LOGIN = new MessageKey("commited_login");
    public static final MessageKey YOU_ARE_ALREADY_AUTHORIZED = new MessageKey("you_are_already_authorized");
    public static final MessageKey INTERNAL_SERVER_ERROR = new MessageKey("internal_server_error");
    public static final MessageKey COMMIT_LOGIN_FAILED = new MessageKey("commit_login_failed");
}
