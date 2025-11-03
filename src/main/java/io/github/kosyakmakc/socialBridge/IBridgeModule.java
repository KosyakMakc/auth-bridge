package io.github.kosyakmakc.socialBridge;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.ITranslationSource;
import io.github.kosyakmakc.socialBridge.Utils.Version;

import java.util.List;

public interface IBridgeModule {
    Version getCompabilityVersion();

    boolean init(ISocialBridge bridge);

    List<ISocialCommand> getSocialCommands();
    List<IMinecraftCommand> getMinecraftCommands();
    List<ITranslationSource> getTranslations();

    String getName();
}
