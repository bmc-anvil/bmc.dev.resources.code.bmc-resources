package bmc.dev.resources.code.bmc_resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;

import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmc_resources.Constants.COMMENT_PREFIX;
import static bmc.dev.resources.code.bmc_resources.Constants.STRUCTURE_SEPARATOR;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;

public class ArchDesignStructureReader {

    public static Function<String, Entry<String, String>> createFolderReadmePair = line -> {
        final String[] lineParts = split(line, STRUCTURE_SEPARATOR);
        final String   folder    = lineParts[0].trim();
        final String   readme    = lineParts.length > 1 ? lineParts[1].trim() : "";
        return entry(folder, readme);
    };

    public static Collector<Entry<String, String>, ?, LinkedHashMap<String, String>> toLinkedHashMap = toMap(Entry::getKey, Entry::getValue,
                                                                                                             (existing, incoming) -> existing,
                                                                                                             LinkedHashMap::new);

    public static Optional<Entry<Integer, Map<String, String>>> readArchStructureFromModel(final String architectureStructureFile, final Log log,
            final Class<? extends ArchitectureGeneratorMojo> mojoClass) {

        try (final BufferedReader archModelReader = new BufferedReader(
                new InputStreamReader(requireNonNull(mojoClass.getResourceAsStream(architectureStructureFile)), UTF_8))) {

            final Map<String, String> foldersAndReadmes = archModelReader.lines()
                                                                         .filter(line -> !isBlank(line) && !line.startsWith(COMMENT_PREFIX))
                                                                         .map(createFolderReadmePair)
                                                                         .sorted(comparingByKey(CASE_INSENSITIVE_ORDER))
                                                                         .collect(toLinkedHashMap);
            log.info("[%s] directories to create according to [%s]".formatted(foldersAndReadmes.size(), architectureStructureFile));

            final Integer maxFolderLength = foldersAndReadmes.keySet()
                                                             .stream()
                                                             .map(String::length)
                                                             .max(Integer::compareTo)
                                                             .orElse(0);

            if (foldersAndReadmes.isEmpty() || maxFolderLength == 0) {
                log.error("Reduction computed foldersAndReadmes Map<String,String> size or maxFolderLength to 0");
                return empty();
            }

            return of(entry(maxFolderLength, foldersAndReadmes));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final NullPointerException npe) {
            throw new IllegalArgumentException("Resource not found on classpath: " + architectureStructureFile);
        }
    }

}
