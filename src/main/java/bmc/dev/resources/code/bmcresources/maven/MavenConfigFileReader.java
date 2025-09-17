package bmc.dev.resources.code.bmcresources.maven;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;

public class MavenConfigFileReader {

    public static Optional<String> readMavenProperty(final AbstractMojo mojo, final MavenProject mavenProject, final String propertyToRead) {

        final Log  log             = mojo.getLog();
        final Path mavenConfigPath = mavenProject.getBasedir().toPath().resolve(".mvn", "maven.config");

        try {
            final List<String> lines = exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
            log.debug("Reading config file: " + lines);

            return lines.stream()
                        .filter(line -> line.contains(propertyToRead))
                        .map(line -> line.split("=", 2)[1])
                        .findFirst();
        }
        catch (final IOException e) {
            log.error("Error reading property [%s] from maven.config".formatted(propertyToRead), e);
            throw new RuntimeException(e);
        }
    }

}
