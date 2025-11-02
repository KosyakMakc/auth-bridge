package io.github.kosyakmakc.socialBridge.Commands.Arguments;

import io.github.kosyakmakc.socialBridge.Utils.MessageKey;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

class GreedyStringCommandArgument extends CommandArgument<String> {
    private final String name;

    public GreedyStringCommandArgument(String name) {
        this.name = name;
    }

    @Override
    public CommandArgumentDataType getDataType() {
        return CommandArgumentDataType.GreedyString;
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
    public String getValue(StringReader args) throws ArgumentFormatException {
        var greedyStringWriter = new StringWriter();

        try {
            args.transferTo(greedyStringWriter);
        } catch (IOException e) {
            throw new ArgumentFormatException(MessageKey.INVALID_ARGUMENT);
        }

        return greedyStringWriter.toString();
    }
}
