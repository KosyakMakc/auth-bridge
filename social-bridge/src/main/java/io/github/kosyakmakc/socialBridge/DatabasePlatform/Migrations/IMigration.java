package io.github.kosyakmakc.socialBridge.DatabasePlatform.Migrations;

import io.github.kosyakmakc.socialBridge.DatabasePlatform.IDatabaseConsumer;

public interface IMigration extends IDatabaseConsumer {
    String getName();
    int getVersion();
}
