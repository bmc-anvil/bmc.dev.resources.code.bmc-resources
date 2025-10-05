package bmc.dev.resources.code.bmcresources.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractArchitectureModel;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractResources;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * Utility class for reading and processing configuration files.
 */
@Slf4j
@UtilityClass
public class ConfigFileReader {

    /**
     * Reads an architecture model config file from the classpath, extracts its content into a List of {@link ArchitectureEntry}.
     * <p>
     * If the configuration file cannot be read, an exception is thrown.
     *
     * @param configFile the name of the configuration file located in the classpath
     *
     * @return a List of {@link ResourceEntry} that can be empty is nothing could be extracted or the file was empty.
     *
     * @throws IllegalArgumentException if the specified configuration file cannot be found or read
     */
    public static List<ArchitectureEntry> readArchitectureModelFile(final String configFile) {

        try (final BufferedReader configFileReader = new BufferedReader(
                new InputStreamReader(requireNonNull(ConfigFileReader.class.getResourceAsStream("/" + configFile)), UTF_8))) {

            final List<ArchitectureEntry> configEntries = extractArchitectureModel.apply(configFileReader);

            log.info("[{}] folders to process according to [{}]", configEntries.size(), configFile);

            if (configEntries.isEmpty()) {
                log.warn("Reduction computed configEntries size to 0, nothing will be processed.");
            }

            return configEntries;
        } catch (final Exception e) {
            log.error("Resource not found on classpath: [{}]", configFile, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Reads a resource's configuration file from the classpath, extracts its content into a List of {@link ResourceEntry}.
     * <p>
     * If the configuration file cannot be read, an exception is thrown.
     *
     * @param configFile the name of the configuration file located in the classpath
     *
     * @return a List of {@link ResourceEntry} that can be empty is nothing could be extracted or the file was empty.
     *
     * @throws IllegalArgumentException if the specified configuration file cannot be found or read
     */
    public static List<ResourceEntry> readResourcesConfigFile(final String configFile) {

        try (final BufferedReader configFileReader = new BufferedReader(
                new InputStreamReader(requireNonNull(ConfigFileReader.class.getResourceAsStream("/" + configFile)), UTF_8))) {

            final List<ResourceEntry> configEntries = extractResources.apply(configFileReader);

            log.info("[{}] resources to process according to [{}]", configEntries.size(), configFile);

            if (configEntries.isEmpty()) {
                log.warn("Reduction computed configEntries size to 0, nothing will be processed.");
            }

            return configEntries;
        } catch (final Exception e) {
            log.error("Resource not found on classpath: [{}]", configFile, e);
            throw new IllegalArgumentException(e);
        }
    }

}
