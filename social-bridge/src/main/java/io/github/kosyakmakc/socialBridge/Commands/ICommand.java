package io.github.kosyakmakc.socialBridge.Commands;

import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.socialBridge.ISocialBridge;

import java.util.List;

public interface ICommand {
    String baseSuffixCommand = "auth";

    void init(ISocialBridge authBridge);
    List<CommandArgument> getArgumentDefinitions();

    String getPermission();
    String getLiteral();
}
