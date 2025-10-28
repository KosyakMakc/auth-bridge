package io.github.kosyakmakc.socialBridge.Commands.Arguments;

import io.github.kosyakmakc.socialBridge.MessageKey;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

class LongCommandArgument extends CommandArgument<Long> {
    private final String name;

    public LongCommandArgument(String name) {
        this.name = name;
    }

    @Override
    public CommandArgumentDataType getDataType() {
        return CommandArgumentDataType.Long;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAutoCompletes() {
        return new String[0];
    }
    
    @Override
    public Long getValue(StringReader args) throws ArgumentFormatException {
        var wordWriter = new StringWriter();
        var charCode = -1;

        try {
            while ((charCode = args.read()) != -1) {
                var symbol = (char) charCode;

                if (Character.isWhitespace(symbol)) {
                    break;
                }

                wordWriter.write(symbol);
            }
        } catch (IOException e) {
            throw new ArgumentFormatException(MessageKey.INVALID_ARGUMENT);
        }

        try {
            return Long.parseLong(wordWriter.toString());
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException(MessageKey.INVALID_ARGUMENT_NOT_A_LONG);
        }
    }
}
