package io.github.kosyakmakc.authBridge.DatabasePlatform.DefaultTranslations;

import io.github.kosyakmakc.authBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.Localization;
import io.github.kosyakmakc.authBridge.MessageKey;

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
                new Localization(getLanguage(), MessageKey.INTERNAL_SERVER_ERROR.key(), "<red>Error has occurred on server side</red>"),
                new Localization(getLanguage(), MessageKey.LOGIN_FROM_MINECRAFT.key(), "Your authorization code - <placeholder-code>"),
                new Localization(getLanguage(), MessageKey.COMMITED_LOGIN.key(), "You are <dark_green>successfully</dark_green> connected <social-platform-name> platform"),

                // social formatted
                new Localization(getLanguage(), MessageKey.YOU_ARE_ALREADY_AUTHORIZED.key(), "You are already authorized on this <social-platform-name> platform"),
                new Localization(getLanguage(), MessageKey.COMMIT_LOGIN_FAILED.key(), "Unable confirm authorization with this code"),

                // shared text
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT.key(), "Argument is invalid"),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_ARE_EMPTY.key(), "Argument is required, but got empty"),
                new Localization(getLanguage(), MessageKey.INVALID_ARGUMENT_NOT_A_NUMBER.key(), "Argument is not a number")
        );
    }
}
