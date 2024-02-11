package api.test.utils;

import api.test.SwapiTest;
import api.test.dto.Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

@RequiredArgsConstructor
public class ConfigRetrieval {

    private static final Logger logger = LoggerFactory.getLogger(SwapiTest.class);

    public static Config retrieve() {
        try (InputStream inputStream = ConfigRetrieval.class.getClassLoader().getResourceAsStream("config.yml")) {
            if (inputStream != null) {
                Yaml yaml = new Yaml();
                Map<String, String> configMap = yaml.load(inputStream);

                return Config.builder().baseUrl(configMap.get("base_url")).build();
            } else {
                logger.error("Could not find the 'config.yml' file.");
            }
        } catch (IOException | RuntimeException e) {
            logger.error("An exception occurred while attempting to read the application config", e);
            throw new RuntimeException("Failed to retrieve configuration", e);
        }

        return null;
    }
}
