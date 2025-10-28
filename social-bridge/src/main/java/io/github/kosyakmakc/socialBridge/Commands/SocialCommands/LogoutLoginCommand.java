package io.github.kosyakmakc.socialBridge.Commands.SocialCommands;

import io.github.kosyakmakc.socialBridge.MessageKey;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.SocialUser;

import java.util.HashMap;
import java.util.List;

public class LogoutLoginCommand extends SocialCommandBase{
    public LogoutLoginCommand() {
        super("logout");
    }

    @Override
    public void execute(SocialUser sender, List<Object> args) {
        var bridge = getBridge();
        var logger = bridge.getLogger();

        var platformName = sender.getPlatform().getPlatformName();
        var socialName = sender.getName();

        var placeholders = new HashMap<String, String>();
        placeholders.put("social-platform-name", sender.getPlatform().getPlatformName());
        placeholders.put("social-name", socialName);

        var isLogout = sender.getPlatform().logoutUser(sender);


        if (isLogout) {
            var minecraftName = sender.getMinecraftUser().getName();
            placeholders.put("minecraft-name", minecraftName);

            logger.info("minecraft(" + minecraftName + ") is logout from " + platformName + " platform.");
            sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.LOGOUT_SUCCESS), placeholders);
        }
        else {
            logger.info("social(" + sender.getName() + ") failed to logout - not authenticated.");
            sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.LOGOUT_FAILED), placeholders);
        }
    }
}
