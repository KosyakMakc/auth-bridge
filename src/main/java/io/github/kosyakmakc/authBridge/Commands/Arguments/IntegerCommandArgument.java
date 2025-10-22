package io.github.kosyakmakc.authBridge.Commands.Arguments;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import io.github.kosyakmakc.authBridge.MessageKey;

class IntegerCommandArgument extends CommandArgument {
    private final String name;

    public IntegerCommandArgument(String name) {
        this.name = name;
    }

    @Override
    public CommandArgumentDataType getDataType() {
        return CommandArgumentDataType.Integer;
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
    public Object getValue(StringReader args) throws ArgumentFormatException {
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
            return Integer.parseInt(wordWriter.toString());
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException(MessageKey.INVALID_ARGUMENT_NOT_A_NUMBER);
        }
    }
}
