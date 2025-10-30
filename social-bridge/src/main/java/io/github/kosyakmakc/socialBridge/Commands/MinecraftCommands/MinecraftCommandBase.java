package io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands;

import io.github.kosyakmakc.socialBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.socialBridge.ISocialBridge;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.socialBridge.Utils.Permissions;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MinecraftCommandBase implements IMinecraftCommand {
    private final String literal;
    private final String permission;
    private final List<CommandArgument> argumentDefinition;
    private ISocialBridge authBridge;

    public MinecraftCommandBase(String literal) {
        this(literal, Permissions.NO_PERMISSION);
    }

    public MinecraftCommandBase(String literal, String permission) {
        this(literal, permission, new ArrayList<>());
    }

    public MinecraftCommandBase(String literal, List<CommandArgument> argumentDefinition) {
        this(literal, Permissions.NO_PERMISSION, argumentDefinition);
    }

    public MinecraftCommandBase(String literal, String permission, List<CommandArgument> argumentDefinition) {
        this.literal = literal;
        this.permission = permission;
        this.argumentDefinition = Collections.unmodifiableList(argumentDefinition);
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public String getPermission() {
        return permission;
    }
    
    @Override
    public List<CommandArgument> getArgumentDefinitions() {
        return argumentDefinition;
    }

    @Override
    public void init(ISocialBridge authBridge) {
        this.authBridge = authBridge;
    }

    public abstract void execute(MinecraftUser sender, List<Object> args);

    @Override
    public void handle(MinecraftUser sender, StringReader argsReader) throws ArgumentFormatException {
        if (authBridge == null) {
            getBridge().getLogger().info(this.getClass().getName() + " - initialization failed, skip handling");
            return;
        }

        var permissionNode = getPermission();
        if (!permissionNode.isEmpty() && sender != null && !sender.HasPermission(permissionNode)) {
            return;
        }

        var arguments = new LinkedList<Object>();

        for (var argument : getArgumentDefinitions()) {
            var valueItem = argument.getValue(argsReader);
            arguments.add(valueItem);
        }

        execute(sender, arguments);
    }

    protected ISocialBridge getBridge() {
        return authBridge;
    }
}
