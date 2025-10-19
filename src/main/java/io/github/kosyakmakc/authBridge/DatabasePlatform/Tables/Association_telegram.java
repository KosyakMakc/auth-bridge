package io.github.kosyakmakc.authBridge.DatabasePlatform.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@DatabaseTable(tableName = Association_telegram.TABLE_NAME)
public class Association_telegram {
    public static final String TABLE_NAME = "association_telegram";

    public static final String ID_FIELD_NAME = "id";
    public static final String MINECRAFT_ID_FIELD_NAME = "minecraft_id";
    public static final String TELEGRAM_ID_FIELD_NAME = "telegram_id";
    public static final String IS_DELETED_FIELD_NAME = "is_deleted";
    public static final String CREATED_AT_FIELD_NAME = "created_at";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private int id;

    @DatabaseField(columnName = MINECRAFT_ID_FIELD_NAME, index = true)
    private UUID minecraftId;

    @DatabaseField(columnName = TELEGRAM_ID_FIELD_NAME, index = true)
    private long telegramId;

    @DatabaseField(columnName = IS_DELETED_FIELD_NAME, index = true)
    private boolean isDeleted;

    @DatabaseField(columnName = CREATED_AT_FIELD_NAME)
    private Date createdAt;

    public Association_telegram() {

    }


    public Association_telegram(UUID minecraftId, long telegramId) {
        this.minecraftId = minecraftId;
        this.telegramId = telegramId;
        this.isDeleted = false;

        var now = Instant.now();
        this.createdAt = Date.from(now);
    }

    public UUID getMinecraftId() {
        return this.minecraftId;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void Delete() {
        this.isDeleted = true;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
