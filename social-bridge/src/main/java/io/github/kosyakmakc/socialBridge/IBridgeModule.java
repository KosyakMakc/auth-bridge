package io.github.kosyakmakc.socialBridge;

import java.util.List;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;

public interface IBridgeModule {
    List<ISocialCommand> getSocialCommands();
    List<IMinecraftCommand> getMinecraftCommands();
}
