package io.github.kosyakmakc.socialBridge.TestEnvironment;

import io.github.kosyakmakc.socialBridge.IAuthBridge;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

public class NullMinecraftPlatform implements IMinecraftPlatform {
    @Override
    public void setAuthBridge(IAuthBridge authBridge) {

    }

    @Override
    public Path getDataDirectory() throws IOException {
        return Path.of(System.getProperty("java.io.tmpdir"), "auth-bridge", UUID.randomUUID().toString());
    }

    @Override
    public Logger getLogger() {
        return Logger.getGlobal();
    }

    @Override
    public MinecraftUser getUser(UUID minecraftId) {
        return null;
    }
}
