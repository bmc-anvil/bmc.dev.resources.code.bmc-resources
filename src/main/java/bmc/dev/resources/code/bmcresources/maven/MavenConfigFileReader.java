package bmc.dev.resources.code.bmcresources.maven;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;

/**
 * Class with utilities to read maven.config files.
 */
@Slf4j
@UtilityClass
public class MavenConfigFileReader {

    /**
     * Retrieves the value of a specified property from the Maven configuration file (.mvn/maven.config).
     * <p>
     * This method searches the configuration file for a line containing the specified property, extracts its value (the portion of the line after the '='
     * character), and returns an {@link Optional} containing the value.
     * <p>
     * If the property is not found, an empty {@link Optional} is returned.
     *
     * @param propertyToRead the name of the property whose value should be retrieved
     *
     * @return an {@link Optional} (of {@link String}) containing the value of the specified property or an {@link Optional#empty()} if the property is not
     * found.
     */
    public static Optional<String> getMavenPropertyValue(final String propertyToRead) {

        final Path mavenConfigPath = getMavenProject().getBasedir().toPath().resolve(".mvn", "maven.config");

        try {
            final List<String> lines = exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
            log.debug(formatColor.apply(CYAN, "Reading config file: [{}]"), lines);

            return lines.stream()
                        .filter(line -> line.contains("="))
                        .filter(line -> line.contains(propertyToRead))
                        .map(line -> line.split("=", 2)[1])
                        .findFirst();
        } catch (final Exception e) {
            log.error("Error reading property [{}] from maven.config", propertyToRead, e);
            throw new RuntimeException(e);
        }
    }

}
