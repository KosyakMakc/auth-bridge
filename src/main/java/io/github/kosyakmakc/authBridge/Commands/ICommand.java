package io.github.kosyakmakc.authBridge.Commands;

import java.util.List;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.Commands.Arguments.CommandArgument;

public interface ICommand {
    public static final String baseSuffixCommand = "auth";

    void init(IAuthBridge authBridge);
    List<CommandArgument> getArgumentDefinitions();

    String getPermission();
    String getLiteral();
}
