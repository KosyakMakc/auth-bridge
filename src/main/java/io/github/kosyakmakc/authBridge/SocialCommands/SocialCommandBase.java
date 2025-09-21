package io.github.kosyakmakc.authBridge.SocialCommands;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;

public abstract class SocialCommandBase implements ISocialCommand {
    public static final String baseSuffixCommand = "authBridge";

    private final String permission;
    private final String commandName;
    private IAuthBridge authBridge;

    public SocialCommandBase(String commandName, String permission) {
        this.permission = permission;
        this.commandName = commandName;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public void init(IAuthBridge authBridge) {
        this.authBridge = authBridge;
    }

    public abstract boolean canExecute(SocialUser sender, String[] args);
    public abstract void execute(SocialUser sender, String[] args);

    @Override
    public void handle(SocialUser sender, String[] args) {
        if (authBridge == null) {
            getBridge().getLogger().info(this.getClass().getName() + " - initialization failed, skip handling");
            return;
        }

        var permissionNode = getPermission();
        var minecraftUser = sender.getMinecraftUser();

        if (!permissionNode.isEmpty()
         && (minecraftUser == null || !minecraftUser.HasPermission(getPermission()))) {
            return;
        }

        if (canExecute(sender, args)) {
            execute(sender, args);
        }
    }

    protected IAuthBridge getBridge() {
        return authBridge;
    }
}
