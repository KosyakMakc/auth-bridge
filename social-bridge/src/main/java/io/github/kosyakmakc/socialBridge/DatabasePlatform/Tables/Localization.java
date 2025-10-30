package io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Localization.TABLE_NAME)
public class Localization {
    public static final String TABLE_NAME = "localization";

    public static final String ID_FIELD_NAME = "id";
    public static final String LANGUAGE_FIELD_NAME = "language";
    public static final String KEY_FIELD_NAME = "key";
    public static final String LOCALIZATION_FIELD_NAME = "localization";

    public static final String LANGUAGE_KEY_INDEX_NAME = "language_key_idx";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private int id;

    @DatabaseField(columnName = LANGUAGE_FIELD_NAME, uniqueIndexName = LANGUAGE_KEY_INDEX_NAME)
    private String language;

    @DatabaseField(columnName = KEY_FIELD_NAME, uniqueIndexName = LANGUAGE_KEY_INDEX_NAME)
    private String key;

    @DatabaseField(columnName = LOCALIZATION_FIELD_NAME)
    private String localization;

    public Localization() {

    }

    public Localization(String language, String key, String localization) {
        this.language = language;
        this.key = key;
        this.localization = localization;
    }

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }
}
