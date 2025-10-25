package io.github.kosyakmakc.authBridge.Commands;

import io.github.kosyakmakc.authBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.authBridge.IAuthBridge;

import java.util.List;

public interface ICommand {
    String baseSuffixCommand = "auth";

    void init(IAuthBridge authBridge);
    List<CommandArgument> getArgumentDefinitions();

    String getPermission();
    String getLiteral();
}
