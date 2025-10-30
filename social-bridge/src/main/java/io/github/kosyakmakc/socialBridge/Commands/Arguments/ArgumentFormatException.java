package io.github.kosyakmakc.socialBridge.Commands.Arguments;

import java.util.logging.Logger;

import io.github.kosyakmakc.socialBridge.Utils.MessageKey;

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
