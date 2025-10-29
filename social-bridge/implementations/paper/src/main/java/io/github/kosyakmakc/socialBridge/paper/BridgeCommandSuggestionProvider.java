package io.github.kosyakmakc.socialBridge.paper;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

public class BridgeCommandSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

    private final CommandArgument argument;

    public BridgeCommandSuggestionProvider(CommandArgument argument) {
        this.argument = argument;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {

        for (var suggest : argument.getAutoCompletes()) {
            builder.suggest(suggest);
        }

        return builder.buildFuture();
    }

}
