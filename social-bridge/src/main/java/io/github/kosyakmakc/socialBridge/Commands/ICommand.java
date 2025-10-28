package io.github.kosyakmakc.socialBridge.Commands;

import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.socialBridge.IAuthBridge;

import java.util.List;

public interface ICommand {
    String baseSuffixCommand = "auth";

    void init(IAuthBridge authBridge);
    List<CommandArgument> getArgumentDefinitions();

    String getPermission();
    String getLiteral();
}
