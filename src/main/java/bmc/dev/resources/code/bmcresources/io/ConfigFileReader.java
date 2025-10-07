package bmc.dev.resources.code.bmcresources.io;

import java.io.BufferedReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.getBufferedReaderFromResource;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Utility class for reading and processing configuration files.
 */
@Slf4j
@UtilityClass
public class ConfigFileReader {

    /**
     * Reads and processes any type of configuration files from the classpath via a given extractor.
     *
     * @param configFile       the name of the configuration file located in the classpath
     * @param contentExtractor the function to extract entries from the BufferedReader
     * @param <T>              the type of entries to be returned
     *
     * @return a List of entries that can be empty if nothing could be extracted or the file was empty
     *
     * @throws IllegalArgumentException if the specified configuration file cannot be found or read
     */
    public static <T> Optional<List<T>> extractConfigFileEntries(final String configFile, final Function<BufferedReader, List<T>> contentExtractor) {

        try (final BufferedReader configFileReader = getBufferedReaderFromResource(configFile, ConfigFileReader.class)) {

            final List<T> configEntries = contentExtractor.apply(configFileReader);

            log.info("[{}] {} to process", configEntries.size(), configFile);

            if (configEntries.isEmpty()) {
                log.warn("Reduction computed configEntries size to 0, nothing will be processed.");
                return empty();
            }

            return of(configEntries);
        } catch (final Exception e) {
            log.error("Error processing config file: [{}]", configFile, e);
            throw new IllegalArgumentException(e);
        }
    }

}
