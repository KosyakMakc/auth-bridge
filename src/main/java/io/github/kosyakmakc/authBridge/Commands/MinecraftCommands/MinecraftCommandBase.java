package io.github.kosyakmakc.authBridge.Commands.MinecraftCommands;

import io.github.kosyakmakc.authBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.authBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.authBridge.Permissions;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MinecraftCommandBase implements IMinecraftCommand {
    private final String literal;
    private final String permission;
    private final List<CommandArgument> argumentDefinition;
    private IAuthBridge authBridge;

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
    public void init(IAuthBridge authBridge) {
        this.authBridge = authBridge;
    }

    public abstract void execute(@Nullable MinecraftUser sender, List<Object> args);

    @Override
    public void handle(@Nullable MinecraftUser sender, StringReader argsReader) throws ArgumentFormatException {
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

    protected IAuthBridge getBridge() {
        return authBridge;
    }
}
