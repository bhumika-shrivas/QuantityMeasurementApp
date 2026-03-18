package com.app.quantitymeasurement.util;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Properties properties = new Properties();

    static {
        try {
            InputStream input =
                    ApplicationConfig.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");

            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("application.properties not found");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}