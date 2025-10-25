package io.github.kosyakmakc.authBridge.Commands.Arguments;

import io.github.kosyakmakc.authBridge.MessageKey;

import java.util.logging.Logger;

public class ArgumentFormatException extends Exception {
    private final MessageKey messageKey;
    
    public ArgumentFormatException(MessageKey messageKey) {
        this.messageKey = messageKey;
    }

    public MessageKey getMessageKey() {
        return messageKey;
    }

    public void logTo(Logger logger) {
        // TODO localize error via static INSTANCE IAuthBridge
        logger.info(messageKey.key());
    }
}
