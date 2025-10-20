package io.github.kosyakmakc.authBridge.MinecraftPlatform.paper;

import io.github.kosyakmakc.authBridge.AuthBridge;
import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftCommands.MinecraftCommandBase;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public final class AuthBridgePaper extends JavaPlugin implements IMinecraftPlatform {
    private IAuthBridge authBridge;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            AuthBridge.Init(this);

            new PaperEventListener(this);

            this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
                var commandsBuilder = Commands.literal(MinecraftCommandBase.baseSuffixCommand);

                for(var bridgeCommand : authBridge.getMinecraftCommands()) {
                    getLogger().info("[DEBUG] registering command - /" + MinecraftCommandBase.baseSuffixCommand + " " + bridgeCommand.getLiteral());
                    commandsBuilder.then(Commands
                            .literal(bridgeCommand.getLiteral())
                            .requires(sender -> sender.getSender().hasPermission(bridgeCommand.getPermission()))
                            .executes(ctx -> {
                                getLogger().info("[DEBUG] command executed");
                                var sender = ctx.getSource().getSender();

                                var mcPlatformUser = sender instanceof Player player ? new BukkitMinecraftUser(player) : null;
                                bridgeCommand.handle(mcPlatformUser, new String[0]);

                                return SINGLE_SUCCESS;
                            }));
                }

                commands.registrar().register(commandsBuilder.build());

                commands.registrar().register(Commands.literal("ks-test").executes(ctx -> {

                    getLogger().info("[DEBUG] ks-test");
                    return SINGLE_SUCCESS;
                }).build());
            });
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
