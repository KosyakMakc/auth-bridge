package io.github.kosyakmakc.authBridge.Commands.Arguments;

import java.io.StringReader;

public abstract class CommandArgument {
    public static CommandArgument ofWord(String name)
    {
        return new WordCommandArgument(name);
    }
    
    public static CommandArgument ofInteger(String name)
    {
        return new IntegerCommandArgument(name);
    }

    public abstract CommandArgumentDataType getDataType();

    public abstract String getName();

    public abstract String[] getAutoCompletes();

    public abstract Object getValue(StringReader args) throws ArgumentFormatException;
}
