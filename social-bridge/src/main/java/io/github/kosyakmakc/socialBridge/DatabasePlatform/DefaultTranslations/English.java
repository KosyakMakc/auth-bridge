package io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations;

import io.github.kosyakmakc.socialBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.Localization;
import io.github.kosyakmakc.socialBridge.Utils.MessageKey;

import java.util.List;

public class English implements ITranslationSource{
    @Override
    public String getLanguage() {
        return LocalizationService.defaultLocale;
    }

    @Override
    public List<Localization> getRecords() {
        return List.of(
                // ingame formatted
                new Localization(getLanguage(), MessageKey.INTERNAL_SERVER_ERROR.key(), "<red>Error has occurred on server side.</red>"),
                new Localization(getLanguage(), MessageKey.LOGIN_FROM_MINECRAFT.key(), "Your authorization code - <placeholder-code>."),
                new Localization(getLanguage(), MessageKey.COMMITED_LOGIN.key(), "You are <dark_green>successfully</dark_green> connected <social-platform-name> platform."),

                // social formatted
                new Localization(getLanguage(), MessageKey.YOU_ARE_ALREADY_AUTHORIZED.key(), "You are already authorized on <social-platform-name> platform."),
                new Localization(getLanguage(), MessageKey.COMMIT_LOGIN_FAILED.key(), "Unable confirm authorization with this code."),


                // shared text
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT.key(), "Argument is invalid."),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_ARE_EMPTY.key(), "Argument is required, but got empty."),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_NOT_A_BOOLEAN.key(), "Argument is not a boolean."),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_NOT_A_INTEGER.key(), "Argument is not a 32-bit integer."),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_NOT_A_LONG.key(), "Argument is not a 64-bit integer."),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_NOT_A_FLOAT.key(), "Argument is not a 32-bit float number."),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_NOT_A_DOUBLE.key(), "Argument is not a 64-bit float number(double)."),

                new Localization(getLanguage(), MessageKey.LOGOUT_SUCCESS.key(), "You(<social-name>) are successfully logout from profile(<minecraft-name>) on <social-platform-name> platform."),
                new Localization(getLanguage(), MessageKey.LOGOUT_FAILED.key(), "You(<social-name>) unable to logout - not authenticated.") // also available <social-platform-name>
        );
    }
}
