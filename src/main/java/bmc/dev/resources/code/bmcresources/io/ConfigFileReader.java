package bmc.dev.resources.code.bmcresources.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractConfiguration;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.getMaxStringLengthFromCollection;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Utility class for reading and processing configuration files.
 */
@Slf4j
@UtilityClass
public class ConfigFileReader {

    /**
     * Reads a configuration file from the classpath, extracts its content into a map of key-value pairs,
     * and computes the maximum string length of the keys in the map.
     * <p>
     * The configuration content and the calculated maximum key length are returned as an entry.
     * If the configuration file cannot be read or its content is invalid, an exception is thrown.
     *
     * @param configFile the name of the configuration file located in the classpath
     *
     * @return an {@link Optional} containing a map entry where the key is the maximum string length
     * of the configuration map's keys and the value is the map of configuration key-value pairs;
     * if the map is empty or invalid, returns an empty {@code Optional}
     *
     * @throws IllegalArgumentException if the specified configuration file cannot be found or read
     */
    public static Optional<Entry<Integer, Map<String, String>>> readConfigFile(final String configFile) {

        try (final BufferedReader configFileReader = new BufferedReader(
                new InputStreamReader(requireNonNull(ConfigFileReader.class.getResourceAsStream("/" + configFile)), UTF_8))) {

            final Map<String, String> configMap    = extractConfiguration.apply(configFileReader);
            final Integer             maxKeyLength = getMaxStringLengthFromCollection.apply(configMap.keySet());

            log.info("[{}] resources to process according to [{}]", configMap.size(), configFile);

            if (configMap.isEmpty()) {
                log.error("Reduction computed configMap Map<String,String> size to 0");
                return empty();
            }

            return of(entry(maxKeyLength, configMap));
        }
        catch (final Exception e) {
            final String errorMessage = "Resource not found on classpath: [%s]".formatted(configFile);
            log.error(errorMessage, e);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

}
