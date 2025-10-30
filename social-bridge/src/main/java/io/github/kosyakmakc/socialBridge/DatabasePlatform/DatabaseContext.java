package io.github.kosyakmakc.socialBridge.DatabasePlatform;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.Association_telegram;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.AuthSession;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.ConfigRow;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.Localization;
import io.github.kosyakmakc.socialBridge.ISocialBridge;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public class DatabaseContext {
    public Dao<ConfigRow, String> configurations;
    public Dao<Association_telegram, Integer> association_telegram;
    public Dao<AuthSession, Integer> sessions;
    public Dao<Localization, Integer> localizations;

    private final ConnectionSource connectionSource;
    private final TransactionManager transactionManager;

    public DatabaseContext(ISocialBridge bridge, JdbcConnectionSource connectionSource) throws SQLException {
        var logger = bridge.getLogger();
        logger.info(connectionSource.getUrl());

        logger.info("DatabasePlatform starts...");
        this.connectionSource = connectionSource;
        this.transactionManager = new TransactionManager(connectionSource);

        configurations = DaoManager.createDao(connectionSource, ConfigRow.class);
        association_telegram = DaoManager.createDao(connectionSource, Association_telegram.class);
        sessions = DaoManager.createDao(connectionSource, AuthSession.class);
        localizations = DaoManager.createDao(connectionSource, Localization.class);
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public void withTransaction(Callable<Void> action) throws SQLException {
        transactionManager.callInTransaction(action);
    }
}
