package io.github.kosyakmakc.authBridge.MinecraftPlatform.paper;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftCommands.MinecraftCommandBase;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.authBridge.AuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public final class AuthBridgePaper extends JavaPlugin implements IMinecraftPlatform {
    private IAuthBridge authBridge;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            AuthBridge.Init(this);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String @NotNull [] args) {
        if (!command.getName().equalsIgnoreCase(MinecraftCommandBase.baseSuffixCommand)) {
            return false;
        }

        var mcPlatformUser = sender instanceof Player player ? new BukkitMinecraftUser(player) : null;

        for(var bridgeCommand : authBridge.getMinecraftCommands()) {
            var handled = bridgeCommand.handle(mcPlatformUser, args);
            if (handled) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String @NotNull [] args) {
        List<String> completions = new ArrayList<>();

        if (!command.getName().equalsIgnoreCase(MinecraftCommandBase.baseSuffixCommand)) {
            return completions;
        }

        var mcPlatformUser = sender instanceof Player player ? new BukkitMinecraftUser(player) : null;

        for(var bridgeCommand : authBridge.getMinecraftCommands()) {
            completions.addAll(bridgeCommand.forTabComplete(mcPlatformUser, args));
        }
        return completions;
    }

    @Override
    public void setAuthBridge(IAuthBridge authBridge) {
        this.authBridge = authBridge;
    }

    @Override
    public @NotNull Path getDataDirectory() throws IOException {
        var dataPath = super.getDataPath();
        Files.createDirectories(dataPath);
        return dataPath;
    }

    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    @Override
    public MinecraftUser getUser(UUID minecraftId) {
        return new BukkitMinecraftUser(Bukkit.getPlayer(minecraftId));
    }
}
