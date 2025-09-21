package io.github.kosyakmakc.authBridge.DatabasePlatform.Migrations;

import com.j256.ormlite.table.TableUtils;
import io.github.kosyakmakc.authBridge.DatabasePlatform.ConfigurationService;
import io.github.kosyakmakc.authBridge.DatabasePlatform.DatabaseContext;
import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.Association_telegram;
import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.AuthSession;
import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.ConfigRow;
import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.Localization;

import java.sql.SQLException;

public class v1_InitialSetup implements IMigration {
    @Override
    public String getName() { return "InitialSetup"; }

    @Override
    public int getVersion() { return 1; }

    @Override
    public Void accept(DatabaseContext databaseContext) throws SQLException {
        var connectionSource = databaseContext.getConnectionSource();

        TableUtils.createTableIfNotExists(connectionSource, ConfigRow.class);
        TableUtils.createTableIfNotExists(connectionSource, Association_telegram.class);
        TableUtils.createTableIfNotExists(connectionSource, AuthSession.class);
        TableUtils.createTableIfNotExists(connectionSource, Localization.class);

        var parameter = ConfigurationService.DATABASE_VERSION;
        var value = Integer.toString(getVersion());

        var record = databaseContext.configurations.queryForId(parameter);
        if (record != null) {
            record.setValue(value);
            databaseContext.configurations.update(record);
        } else {
            var newRecord = new ConfigRow(parameter, value);
            databaseContext.configurations.create(newRecord);
        }

        return null;
    }
}
