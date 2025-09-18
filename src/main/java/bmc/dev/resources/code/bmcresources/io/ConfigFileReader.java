package bmc.dev.resources.code.bmcresources.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractConfiguration;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.getMaxStringLengthFromCollection;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
public class ConfigFileReader {

    private ConfigFileReader() {}

    public static Optional<Entry<Integer, Map<String, String>>> readConfigFile(final String configFile) {

        try (final BufferedReader archModelReader = new BufferedReader(
                new InputStreamReader(requireNonNull(ConfigFileReader.class.getResourceAsStream(configFile)), UTF_8))) {

            final Map<String, String> resourcesConfigMap = extractConfiguration.apply(archModelReader);
            final Integer             maxFolderLength    = getMaxStringLengthFromCollection.apply(resourcesConfigMap.keySet());

            log.info("[{}] resources to process according to [{}]", resourcesConfigMap.size(), configFile);

            if (resourcesConfigMap.isEmpty() || maxFolderLength == 0) {
                log.error("Reduction computed resourcesConfigMap Map<String,String> size or maxFolderLength to 0");
                return empty();
            }

            return of(entry(maxFolderLength, resourcesConfigMap));
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("Resource not found on classpath: [%s]".formatted(configFile), e);
        }
    }

}
