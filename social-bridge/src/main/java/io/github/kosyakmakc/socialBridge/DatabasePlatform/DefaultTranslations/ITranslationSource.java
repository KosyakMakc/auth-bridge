package io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations;

import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.Localization;

import java.util.List;

public interface ITranslationSource {
    String getLanguage();
    List<Localization> getRecords();
}
