package ru.cft.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@UtilityClass
public final class PropertiesUtil {
    private static final String PROPERTIES_FILENAME = "application.properties";

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream propertiesInputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
            PROPERTIES.load(propertiesInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String propertyName, String defaultValue) {
        return PROPERTIES.getProperty(propertyName, defaultValue);
    }

    public static Map<String, String> getProperties(String commonPrefix) {
        return PROPERTIES.entrySet().stream()
                .filter(e -> ((String) e.getKey()).startsWith(commonPrefix))
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
    }
}
