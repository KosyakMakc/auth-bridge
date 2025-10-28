package io.github.kosyakmakc.socialBridge.Commands.SocialCommands;

import io.github.kosyakmakc.socialBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.socialBridge.IAuthBridge;
import io.github.kosyakmakc.socialBridge.Permissions;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.SocialUser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class SocialCommandBase implements ISocialCommand {
    private final String permission;
    private final String commandName;
    private final List<CommandArgument> argumentDefinition;
    private IAuthBridge authBridge;

    public SocialCommandBase(String commandName) {
        this(commandName, Permissions.NO_PERMISSION);
    }

    public SocialCommandBase(String commandName, String permission) {
        this(commandName, permission, new ArrayList<CommandArgument>());
    }

    public SocialCommandBase(String commandName, List<CommandArgument> argumentDefinition) {
        this(commandName, Permissions.NO_PERMISSION, argumentDefinition);
    }

    public SocialCommandBase(String commandName, String permission, List<CommandArgument> argumentDefinition) {
        this.permission = permission;
        this.commandName = commandName;
        this.argumentDefinition = Collections.unmodifiableList(argumentDefinition);
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getLiteral() {
        return commandName;
    }
    
    @Override
    public List<CommandArgument> getArgumentDefinitions() {
        return argumentDefinition;
    }

    @Override
    public void init(IAuthBridge authBridge) {
        this.authBridge = authBridge;
    }

    public abstract void execute(SocialUser sender, List<Object> args);

    @Override
    public void handle(SocialUser sender, StringReader argsReader) throws ArgumentFormatException {
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

        var arguments = new LinkedList<Object>();

        for (var argument : getArgumentDefinitions()) {
            var valueItem = argument.getValue(argsReader);
            arguments.add(valueItem);
        }

        execute(sender, arguments);
    }

    protected IAuthBridge getBridge() {
        return authBridge;
    }
}
