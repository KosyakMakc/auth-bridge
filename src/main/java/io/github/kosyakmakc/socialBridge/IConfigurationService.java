package io.github.kosyakmakc.socialBridge;

public interface IConfigurationService {
    String get(String parameter, String defaultValue);
    @SuppressWarnings("unused")
    boolean set(String parameter, String value);
}
