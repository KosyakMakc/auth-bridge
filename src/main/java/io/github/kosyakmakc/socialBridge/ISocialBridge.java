package io.github.kosyakmakc.socialBridge;

import io.github.kosyakmakc.socialBridge.DatabasePlatform.ConfigurationService;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.IDatabaseConsumer;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.ISocialPlatform;

import java.lang.Runtime.Version;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

public interface ISocialBridge {
    Logger getLogger();
    Version getVersion();

    LocalizationService getLocalizationService();
    ConfigurationService getConfigurationService();
    void queryDatabase(IDatabaseConsumer action) throws SQLException;

    @SuppressWarnings("unused")
    boolean registerSocialPlatform(ISocialPlatform socialPlatform);
    @SuppressWarnings("unused")
    Collection<ISocialPlatform> getSocialPlatforms();
    @SuppressWarnings("unused")
    <T extends ISocialPlatform> T getSocialPlatform(Class<T> tClass);

    @SuppressWarnings("unused")
    IMinecraftPlatform getMinecraftPlatform();

    @SuppressWarnings("unused")
    boolean registerModule(IBridgeModule module);
    Collection<IBridgeModule> getModules();
    @SuppressWarnings("unused")
    <T extends IBridgeModule> T getModule(Class<T> tClass);

    void Start();
}
