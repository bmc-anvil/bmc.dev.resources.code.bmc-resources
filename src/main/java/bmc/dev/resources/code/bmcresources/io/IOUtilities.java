package bmc.dev.resources.code.bmcresources.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class IOUtilities {

    public static void copySingleResource(final AbstractMojo mojo, final String sourceDir, final Path targetPath, final String resource) {

        final String fullReadmeResourcePath = sourceDir + resource;

        try (final InputStream readmeFileStream = mojo.getClass().getResourceAsStream(fullReadmeResourcePath)) {
            if (readmeFileStream == null) {
                mojo.getLog().warn("Could not find resource [%s]".formatted(fullReadmeResourcePath));
            }
            else {
                copy(readmeFileStream, targetPath.resolve(resource), REPLACE_EXISTING);
            }
        }
        catch (final IOException ioe) {
            mojo.getLog().error("Error copying architecture readme: [%s]".formatted(resource), ioe);
        }
    }

    public static void createDirectory(final AbstractMojo mojo, final Path target) {

        try {
            createDirectories(target);
        }
        catch (final IOException ioe) {
            mojo.getLog().error("could not create directory [%s]".formatted(target), ioe);
        }
    }

    public static List<String> readAllLinesFromFile(final AbstractMojo mojo, final Path mavenConfigPath) {

        try {
            return exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAllLinesToFile(final AbstractMojo mojo, final Collection<String> linesToWrite, final Path mavenConfigPath) {

        try {
            mojo.getLog().debug("writing all lines [%s]".formatted(linesToWrite));
            write(mavenConfigPath, linesToWrite, UTF_8, CREATE, TRUNCATE_EXISTING);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
