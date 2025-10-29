package io.github.kosyakmakc.socialBridge.DatabasePlatform;

import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.ITranslationSource;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.Localization;
import io.github.kosyakmakc.socialBridge.Utils.MessageKey;
import io.github.kosyakmakc.socialBridge.ISocialBridge;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public record LocalizationService(ISocialBridge bridge) {
    static public final String defaultLocale = Locale.US.getLanguage();

    public String getMessage(String locale, MessageKey key) {
        var logger = bridge.getLogger();
        try {
            AtomicReference<String> result = new AtomicReference<>(key.key());
            bridge.queryDatabase(databaseContext -> {
                var records = databaseContext.localizations.queryBuilder()
                        .where()
                            .eq(Localization.LANGUAGE_FIELD_NAME, locale)
                            .and()
                            .eq(Localization.KEY_FIELD_NAME, key.key())
                        .query();

                if (records.size() == 1) {
                    result.set(records.getFirst().getLocalization());
                } else {
                    if (!locale.equalsIgnoreCase(defaultLocale)) {
                        result.set(getMessage(defaultLocale, key));
                    }
                }

                return null;
            });
            return result.get();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "failed localization search", e);
            return "internal database error";
        }
    }

    public boolean setLocalization(String locale, MessageKey key, String localization) {
        var logger = bridge.getLogger();
        try {
            bridge.queryDatabase(databaseContext -> {
                var records = databaseContext.localizations.queryBuilder()
                        .where()
                        .eq(Localization.LANGUAGE_FIELD_NAME, locale)
                        .and()
                        .eq(Localization.KEY_FIELD_NAME, key.key())
                        .query();
                if (records.size() == 1) {
                    var record = records.getFirst();
                    record.setLocalization(localization);
                    databaseContext.localizations.update(record);
                } else {
                    var newRecord = new Localization(locale, key.key(), localization);
                    databaseContext.localizations.create(newRecord);
                }

                return null;
            });
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "failed localization update", e);
            return false;
        }
    }

    public void restoreDatabase() {
        var logger = bridge.getLogger();

        AtomicInteger insertedRecords = new AtomicInteger();
        var allRecords = 0;

        var localizationSources = ServiceLoader.load(ITranslationSource.class, ITranslationSource.class.getClassLoader()).stream().map(ServiceLoader.Provider::get).toList();
        logger.info("Localization sources(" + localizationSources.size() + "):");
        for (var source : localizationSources) {
            logger.info("\t\t" + source.getClass().getName());
            for (var record : source.getRecords()) {
                allRecords++;
                try {
                    bridge.queryDatabase(databaseContext -> {
                        databaseContext.localizations.create(record);

                        insertedRecords.getAndIncrement();
                        return null;
                    });
                } catch (SQLException ignored) { }
            }
        }

        if (insertedRecords.get() > 0) {
            logger.info("restored localizations in database (" + insertedRecords + "/" + allRecords + ")");
        }
    }
}
