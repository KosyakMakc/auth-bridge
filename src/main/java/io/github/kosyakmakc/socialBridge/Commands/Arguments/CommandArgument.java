package io.github.kosyakmakc.socialBridge.Commands.Arguments;

import java.io.StringReader;

public abstract class CommandArgument<T> {
    @SuppressWarnings("unused")
    public static CommandArgument<String> ofWord(String name)
    {
        return new WordCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<String> ofString(String name)
    {
        return new StringCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<String> ofGreedyString(String name)
    {
        return new GreedyStringCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<Integer> ofInteger(String name)
    {
        return new IntegerCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<Long> ofLong(String name)
    {
        return new LongCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<Float> ofFloat(String name)
    {
        return new FloatCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<Double> ofDouble(String name)
    {
        return new DoubleCommandArgument(name);
    }

    @SuppressWarnings("unused")
    public static CommandArgument<Boolean> ofBoolean(String name)
    {
        return new BooleanCommandArgument(name);
    }

    public abstract CommandArgumentDataType getDataType();

    public abstract String getName();

    public abstract String[] getAutoCompletes();

    public abstract T getValue(StringReader args) throws ArgumentFormatException;
}
