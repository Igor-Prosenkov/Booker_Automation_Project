package core.clients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClients {

    private final String baseUrl;

    public APIClients() {
        this.baseUrl = determineBaseUrl();
    }

    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Не найден файл конфигурации" + configFileName);

            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Не найден файл конфигурации" + configFileName, e);
        }

        return properties.getProperty("baseUrl");
    }
}
