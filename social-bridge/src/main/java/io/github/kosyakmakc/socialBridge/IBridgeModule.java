package io.github.kosyakmakc.socialBridge;

import java.lang.Runtime.Version;
import java.util.List;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;

public interface IBridgeModule {
    Version getCompabilityVersion();

    List<ISocialCommand> getSocialCommands();
    List<IMinecraftCommand> getMinecraftCommands();

    String getName();
}
