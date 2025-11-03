package io.github.kosyakmakc.socialBridge;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.English;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.ITranslationSource;
import io.github.kosyakmakc.socialBridge.Utils.Version;

import java.util.List;

public class DefaultModule implements IBridgeModule{

    public static final Version VERSION = new Version(0, 1, 0);
    public static final List<ITranslationSource> translationSources = List.of(new English());
    public static final List<IMinecraftCommand> minecraftCommands = List.of();
    public static final List<ISocialCommand> socialCommands = List.of();

    @Override
    public Version getCompabilityVersion() {
        return VERSION;
    }

    @Override
    public boolean init(ISocialBridge bridge) {
        return true;
    }

    @Override
    public List<ISocialCommand> getSocialCommands() {
        return socialCommands;
    }

    @Override
    public List<IMinecraftCommand> getMinecraftCommands() {
        return minecraftCommands;
    }

    @Override
    public List<ITranslationSource> getTranslations() {
        return translationSources;
    }

    @Override
    public String getName() {
        return "Default module";
    }
}
