package bmc.dev.resources.code.bmc_resources;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmc_resources.Constants.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toList;

public class MavenConfigFileUpdater {

    private static final String            CLI_MVN_PROPERTY_NAME            = "-D" + PROPERTY_ARCH_COMPLETED;
    private static final Predicate<String> hasArchitectureCompletedProperty = line -> !line.startsWith(CLI_MVN_PROPERTY_NAME);

    public static void updateArchitectureCompletion(final Log log) {

        final Path mavenConfigPath = get(".mvn", "maven.config");

        try {
            createDirectories(mavenConfigPath.getParent());

            final List<String> lines = exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
            final List<String> filtered = lines.stream()
                                               .filter(hasArchitectureCompletedProperty)
                                               .collect(toList());

            filtered.add(CLI_MVN_PROPERTY_NAME + "=true");

            write(mavenConfigPath, filtered, UTF_8, CREATE, TRUNCATE_EXISTING);

            log.info("%s%sUpdated [%s] with [%s]=true%s\n".formatted(COLOR_BOLD, COLOR_RED, mavenConfigPath, CLI_MVN_PROPERTY_NAME, COLOR_RESET));

        } catch (final IOException e) {
            log.error("Failed to update .mvn/maven.config with architecture completion flag", e);
            throw new RuntimeException(e);
        }
    }

}
