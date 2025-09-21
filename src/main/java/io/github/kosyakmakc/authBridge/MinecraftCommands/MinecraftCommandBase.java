package io.github.kosyakmakc.authBridge.MinecraftCommands;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;

import java.util.List;

public abstract class MinecraftCommandBase implements IMinecraftCommand {
    public static final String baseSuffixCommand = "authBridge";

    private final String permission;
    private IAuthBridge authBridge;

    public MinecraftCommandBase(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public List<String> forTabComplete(MinecraftUser sender, String[] userWords) {
        return List.of();
    }

    @Override
    public void init(IAuthBridge authBridge) {
        this.authBridge = authBridge;
    }

    public abstract boolean canExecute(MinecraftUser sender, String[] args);
    public abstract void execute(MinecraftUser sender, String[] args);

    @Override
    public Boolean handle(MinecraftUser sender, String[] args) {
        if (authBridge == null) {
            getBridge().getLogger().info(this.getClass().getName() + " - initialization failed, skip handling");
            return false;
        }

        var permissionNode = getPermission();
        if (!permissionNode.isEmpty() && !sender.HasPermission(permissionNode)) {
            return false;
        }

        if (canExecute(sender, args)) {
            execute(sender, args);
            return true;
        }
        return false;
    }

    protected IAuthBridge getBridge() {
        return authBridge;
    }
}
