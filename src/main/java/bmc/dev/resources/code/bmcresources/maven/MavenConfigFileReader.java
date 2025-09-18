package bmc.dev.resources.code.bmcresources.maven;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.GetMavenProject;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;

@Slf4j
public class MavenConfigFileReader {

    private MavenConfigFileReader() {}

    public static Optional<String> readMavenProperty(final String propertyToRead) {

        final Path mavenConfigPath = GetMavenProject().getBasedir().toPath().resolve(".mvn", "maven.config");

        try {
            final List<String> lines = exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
            log.debug("Reading config file: {} ", lines);

            return lines.stream()
                        .filter(line -> line.contains(propertyToRead))
                        .map(line -> line.split("=", 2)[1])
                        .findFirst();
        }
        catch (final IOException e) {
            log.error("Error reading property [{}] from maven.config", propertyToRead, e);
            throw new RuntimeException(e);
        }
    }

}
