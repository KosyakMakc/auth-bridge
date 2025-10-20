package io.github.kosyakmakc.authBridge.MinecraftPlatform.paper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PaperEventListener implements Listener {
    public PaperEventListener(AuthBridgePaper minecraftPlatform) {
        minecraftPlatform.getServer().getPluginManager().registerEvents(this, minecraftPlatform);
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().updateCommands();
    }
}
