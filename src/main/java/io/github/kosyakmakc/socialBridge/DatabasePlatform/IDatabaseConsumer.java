package io.github.kosyakmakc.socialBridge.DatabasePlatform;

import java.sql.SQLException;

@FunctionalInterface
public interface IDatabaseConsumer {
    Void accept(DatabaseContext databaseContext) throws SQLException;
}
