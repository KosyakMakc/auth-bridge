package io.github.kosyakmakc.socialBridge.Commands.Arguments;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import io.github.kosyakmakc.socialBridge.Utils.MessageKey;

class IntegerCommandArgument extends CommandArgument<Integer> {
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
    public Integer getValue(StringReader args) throws ArgumentFormatException {
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
            return (int) Double.parseDouble(wordWriter.toString());
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException(MessageKey.INVALID_ARGUMENT_NOT_A_INTEGER);
        }
    }
}
