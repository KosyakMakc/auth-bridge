package io.github.kosyakmakc.authBridge.TestEnvironment;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;

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
