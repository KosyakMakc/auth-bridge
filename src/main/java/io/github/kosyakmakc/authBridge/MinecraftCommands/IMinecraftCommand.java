package io.github.kosyakmakc.authBridge.MinecraftCommands;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMinecraftCommand {
    void init(IAuthBridge authBridge);
    String getPermission();

    String getLiteral();

    List<String> forTabComplete(@Nullable MinecraftUser sender, String[] userWords);
    Boolean handle(@Nullable MinecraftUser sender, String[] args);
}
