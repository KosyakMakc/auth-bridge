package io.github.kosyakmakc.authBridge.Commands.MinecraftCommands;

import io.github.kosyakmakc.authBridge.Commands.ICommand;
import io.github.kosyakmakc.authBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import org.jetbrains.annotations.Nullable;

public interface IMinecraftCommand extends ICommand {
    void handle(@Nullable MinecraftUser sender, String args) throws ArgumentFormatException;
}
