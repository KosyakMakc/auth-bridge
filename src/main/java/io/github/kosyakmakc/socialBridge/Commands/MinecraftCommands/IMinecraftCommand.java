package io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands;

import io.github.kosyakmakc.socialBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.socialBridge.Commands.ICommand;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;

import java.io.StringReader;

public interface IMinecraftCommand extends ICommand {
    void handle(MinecraftUser sender, StringReader argsReader) throws ArgumentFormatException;
}
