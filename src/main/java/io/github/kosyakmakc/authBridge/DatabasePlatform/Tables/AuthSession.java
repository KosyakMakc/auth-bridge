package io.github.kosyakmakc.authBridge.DatabasePlatform.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@DatabaseTable(tableName = AuthSession.TABLE_NAME)
public class AuthSession {
    public static final String TABLE_NAME = "auth_session";

    public static final String ID_FIELD_NAME = "id";
    public static final String MINECRAFT_ID_FIELD_NAME = "minecraft_id";
    public static final String AUTH_CODE_FIELD_NAME = "auth_code";
    public static final String CREATED_AT_FIELD_NAME = "created_at";
    public static final String EXPIRED_AT_FIELD_NAME = "expired_at";
    public static final String IS_SPENT_FIELD_NAME = "is_spent";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private int id;

    @DatabaseField(columnName = MINECRAFT_ID_FIELD_NAME, index = true)
    private UUID minecraftId;

    @DatabaseField(columnName = AUTH_CODE_FIELD_NAME)
    private int authCode;

    @DatabaseField(columnName = IS_SPENT_FIELD_NAME)
    private boolean isSpent;

    @DatabaseField(columnName = CREATED_AT_FIELD_NAME)
    private Date createdAt;

    @DatabaseField(columnName = EXPIRED_AT_FIELD_NAME, index = true)
    private Date expiredAt;

    public AuthSession() {

    }

    public AuthSession(UUID minecraftId, int authCode, Duration expiration) {
        this.minecraftId = minecraftId;
        this.authCode = authCode;
        var now = Instant.now();
        this.createdAt = Date.from(now);
        this.expiredAt = Date.from(now.plus(expiration));
        this.isSpent = false;
    }

    public UUID getMinecraftId() {
        return minecraftId;
    }

    public int getAuthCode() {
        return authCode;
    }

    public boolean isSpent() {
        return isSpent;
    }

    public void spend() {
        this.isSpent = true;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

}
