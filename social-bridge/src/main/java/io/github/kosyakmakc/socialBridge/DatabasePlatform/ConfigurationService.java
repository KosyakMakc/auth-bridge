package io.github.kosyakmakc.socialBridge.DatabasePlatform;

import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.ConfigRow;
import io.github.kosyakmakc.socialBridge.IAuthBridge;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public record ConfigurationService(IAuthBridge bridge) {
    public static final String DATABASE_VERSION = "DATABASE_VERSION";

    public String get(String parameter, String defaultValue) {
        try {
            AtomicReference<String> result = new AtomicReference<>(defaultValue);
            bridge.queryDatabase(databaseContext -> {
                var record = databaseContext.configurations.queryForId(parameter);
                if (record != null) {
                    result.set(record.getValue());
                }

                return null;
            });
            return result.get();
        } catch (SQLException e) {
            return defaultValue;
        }
    }

    public boolean set(String parameter, String value) {
        try {
            bridge.queryDatabase(databaseContext -> {
                var record = databaseContext.configurations.queryForId(parameter);
                if (record != null) {
                    record.setValue(value);
                    databaseContext.configurations.update(record);
                } else {
                    var newRecord = new ConfigRow(parameter, value);
                    databaseContext.configurations.create(newRecord);
                }

                return null;
            });
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int getDatabaseVersion() {
        var rawVersion = get(DATABASE_VERSION, "");
        try {
            return Integer.parseInt(rawVersion);
        }
        catch (NumberFormatException err) {
            return -1;
        }
    }
}
