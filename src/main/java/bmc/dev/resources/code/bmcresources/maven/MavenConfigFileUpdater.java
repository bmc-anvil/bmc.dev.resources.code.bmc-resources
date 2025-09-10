package bmc.dev.resources.code.bmcresources.maven;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toList;

public class MavenConfigFileUpdater {

    private static final Function<String, String>    CLI_MVN_PROPERTY     = "-D%s"::formatted;
    private static final BiPredicate<String, String> hasCompletedProperty =
            (line, propertyCompleted) -> !line.startsWith(CLI_MVN_PROPERTY.apply(propertyCompleted));

    public static void updateMavenConfig(final AbstractMojo mojoClass, final String propertyCompleted) {

        final Log log = mojoClass.getLog();

        final Path   mavenConfigPath      = get(".mvn", "maven.config");
        final String cliPropertyCompleted = CLI_MVN_PROPERTY.apply(propertyCompleted);
        try {
            createDirectories(mavenConfigPath.getParent());

            final List<String> lines = exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
            final List<String> filtered = lines.stream()
                                               .filter(line -> hasCompletedProperty.test(line, propertyCompleted))
                                               .collect(toList());

            filtered.add(cliPropertyCompleted + "=true");

            write(mavenConfigPath, filtered, UTF_8, CREATE, TRUNCATE_EXISTING);

            log.info("%s%sUpdated [%s] with [%s]=true%s".formatted(COLOR_BOLD, COLOR_RED, mavenConfigPath, cliPropertyCompleted, COLOR_RESET));
            log.info("");

        }
        catch (final IOException e) {
            log.error("Failed to update .mvn/maven.config with architecture completion flag", e);
            throw new RuntimeException(e);
        }
    }

}
