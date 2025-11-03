package io.github.kosyakmakc.socialBridge;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.English;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.ITranslationSource;
import io.github.kosyakmakc.socialBridge.Utils.Version;

import java.util.List;

public class DefaultModule implements IBridgeModule{
    @Override
    public Version getCompabilityVersion() {
        return new Version(0, 1, 0);
    }

    @Override
    public boolean init(ISocialBridge bridge) {
        return true;
    }

    @Override
    public List<ISocialCommand> getSocialCommands() {
        return List.of();
    }

    @Override
    public List<IMinecraftCommand> getMinecraftCommands() {
        return List.of();
    }

    @Override
    public List<ITranslationSource> getTranslations() {
        return List.of(new English());
    }

    @Override
    public String getName() {
        return "Default module";
    }
}
