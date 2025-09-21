package io.github.kosyakmakc.authBridge.MinecraftPlatform.paper;

import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BukkitMinecraftUser extends MinecraftUser {
    private final Player player;

    public BukkitMinecraftUser(Player player) {
        super();
        this.player = player;
    }

    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getId() {
        return player.getUniqueId();
    }

    public String getLocale() {
        return player.locale().getLanguage();
    }

    @Override
    public boolean HasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message, HashMap<String, String> placeholders) {
        var builder = MiniMessage.builder()
                                 .tags(TagResolver.builder()
                                                  .resolver(StandardTags.defaults())
                                                  .build());

        for (var placeholderKey : placeholders.keySet()) {
            builder.editTags(x -> x.resolver(Placeholder.component(placeholderKey, Component.text(placeholders.get(placeholderKey)))));
        }
        var builtMessage = builder.build().deserialize(message);
        player.sendMessage(builtMessage);
    }
}
