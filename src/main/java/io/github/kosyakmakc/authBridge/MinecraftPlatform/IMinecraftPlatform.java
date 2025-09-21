package io.github.kosyakmakc.authBridge.MinecraftPlatform;

import io.github.kosyakmakc.authBridge.IAuthBridge;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public interface IMinecraftPlatform {
    void setAuthBridge(IAuthBridge authBridge);

    java.nio.file.Path getDataDirectory() throws IOException;

    Logger getLogger();

    MinecraftUser getUser(UUID minecraftId);
}
