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

@Slf4j
@UtilityClass
public class MavenConfigFileReader {

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
