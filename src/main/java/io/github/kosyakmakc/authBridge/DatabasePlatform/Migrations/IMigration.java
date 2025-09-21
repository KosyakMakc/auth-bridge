package io.github.kosyakmakc.authBridge.DatabasePlatform.Migrations;

import io.github.kosyakmakc.authBridge.DatabasePlatform.IDatabaseConsumer;

public interface IMigration extends IDatabaseConsumer {
    String getName();
    int getVersion();
}
