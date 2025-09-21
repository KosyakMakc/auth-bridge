package io.github.kosyakmakc.authBridge.DatabasePlatform;

import java.sql.SQLException;

@FunctionalInterface
public interface IDatabaseConsumer {
    Void accept(DatabaseContext databaseContext) throws SQLException;
}
