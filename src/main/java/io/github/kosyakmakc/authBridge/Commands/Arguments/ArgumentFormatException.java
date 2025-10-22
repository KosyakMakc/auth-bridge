package io.github.kosyakmakc.authBridge.Commands.Arguments;

import io.github.kosyakmakc.authBridge.MessageKey;

public class ArgumentFormatException extends Exception {
    private final MessageKey messageKey;
    
    public ArgumentFormatException(MessageKey messageKey) {
        this.messageKey = messageKey;
    }

    public MessageKey getMessageKey() {
        return messageKey;
    }
}
