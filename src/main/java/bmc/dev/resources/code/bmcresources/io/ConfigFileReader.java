package bmc.dev.resources.code.bmcresources.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmcresources.io.ConfigFileUtilities.extractConfiguration;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileUtilities.getMaxStringLengthFromCollection;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class ConfigFileReader {

    public static Optional<Entry<Integer, Map<String, String>>> readConfigFile(final AbstractMojo mojoClass, final String configFile) {

        final Log log = mojoClass.getLog();

        try (final BufferedReader archModelReader = new BufferedReader(
                new InputStreamReader(requireNonNull(mojoClass.getClass().getResourceAsStream(configFile)), UTF_8))) {

            final Map<String, String> resourcesConfigMap = extractConfiguration.apply(archModelReader);

            log.info("[%s] resources to create according to [%s]".formatted(resourcesConfigMap.size(), configFile));

            final Integer maxFolderLength = getMaxStringLengthFromCollection.apply(resourcesConfigMap.keySet());

            if (resourcesConfigMap.isEmpty() || maxFolderLength == 0) {
                log.error("Reduction computed resourcesConfigMap Map<String,String> size or maxFolderLength to 0");
                return empty();
            }

            return of(entry(maxFolderLength, resourcesConfigMap));
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        catch (final NullPointerException npe) {
            throw new IllegalArgumentException("Resource not found on classpath: " + configFile);
        }
    }

}
