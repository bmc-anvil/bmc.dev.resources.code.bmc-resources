package bmc.dev.resources.code.bmcresources.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatCyan;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatYellowBold;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Slf4j
public class IOUtilities {

    private IOUtilities() {}

    public static void copySingleResource(final String sourceDir, final Path targetPath, final String resource) {

        final String fullReadmeResourcePath = sourceDir + resource;

        try (final InputStream readmeFileStream = IOUtilities.class.getResourceAsStream(fullReadmeResourcePath)) {
            if (readmeFileStream == null) {
                log.warn(formatYellowBold("Could not find resource [{}]"), fullReadmeResourcePath);
            }
            else {
                copy(readmeFileStream, targetPath.resolve(resource), REPLACE_EXISTING);
            }
        }
        catch (final IOException ioe) {
            log.error("Error copying architecture readme: [{}]", resource, ioe);
        }
    }

    public static void createDirectory(final Path target) {

        try {
            createDirectories(target);
        }
        catch (final IOException ioe) {
            log.error("could not create directory [{}]", target, ioe);
        }
    }

    public static List<String> readAllLinesFromFile(final Path mavenConfigPath) {

        try {
            return exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAllLinesToFile(final Collection<String> linesToWrite, final Path mavenConfigPath) {

        try {
            log.debug(formatCyan("writing all lines [{}]"), linesToWrite);
            write(mavenConfigPath, linesToWrite, UTF_8, CREATE, TRUNCATE_EXISTING);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
