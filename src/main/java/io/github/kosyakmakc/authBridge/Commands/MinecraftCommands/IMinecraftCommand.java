package io.github.kosyakmakc.authBridge.Commands.MinecraftCommands;

import io.github.kosyakmakc.authBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.authBridge.Commands.ICommand;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;

public interface IMinecraftCommand extends ICommand {
    void handle(@Nullable MinecraftUser sender, StringReader argsReader) throws ArgumentFormatException;
}
