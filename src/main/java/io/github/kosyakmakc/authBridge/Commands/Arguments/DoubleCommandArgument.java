package io.github.kosyakmakc.authBridge.Commands.Arguments;

import io.github.kosyakmakc.authBridge.MessageKey;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

class DoubleCommandArgument extends CommandArgument<Double> {
    private final String name;

    public DoubleCommandArgument(String name) {
        this.name = name;
    }

    @Override
    public CommandArgumentDataType getDataType() {
        return CommandArgumentDataType.Double;
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
    public Double getValue(StringReader args) throws ArgumentFormatException {
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
            return Double.parseDouble(wordWriter.toString());
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException(MessageKey.INVALID_ARGUMENT_NOT_A_DOUBLE);
        }
    }
}
