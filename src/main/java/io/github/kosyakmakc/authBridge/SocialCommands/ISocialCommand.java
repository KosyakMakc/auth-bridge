package io.github.kosyakmakc.authBridge.SocialCommands;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;

public interface ISocialCommand {
    void init(IAuthBridge authBridge);
    String getPermission();
    String getCommandName();
    void handle(SocialUser sender, String[] args);
}
