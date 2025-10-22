package io.github.kosyakmakc.authBridge.Commands.SocialCommands;

import io.github.kosyakmakc.authBridge.Commands.ICommand;
import io.github.kosyakmakc.authBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;

public interface ISocialCommand extends ICommand {
    void handle(SocialUser sender, String args) throws ArgumentFormatException;
}
