package io.github.kosyakmakc.authBridge.DatabasePlatform.DefaultTranslations;

import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.Localization;

import java.util.List;

public interface ITranslationSource {
    String getLanguage();
    List<Localization> getRecords();
}
