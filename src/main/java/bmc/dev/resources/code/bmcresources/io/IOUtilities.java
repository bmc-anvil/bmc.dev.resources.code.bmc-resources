package bmc.dev.resources.code.bmcresources.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.YELLOW;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * A utility class for common I/O operations such as copying resources, creating directories,
 * reading from files, and writing to files.
 * <p>
 * This class handles exceptions so clients do not need to worry about them. *
 */
@Slf4j
@UtilityClass
public class IOUtilities {

    /**
     * Copies the contents of a specified folder within a JAR file into a specified target folder.
     * Subdirectories and files from the source folder are recursively copied to the target location.
     *
     * @param sourceFolderInJar the path to the folder within the JAR file to be copied (relative path within the JAR)
     * @param targetFolder      the destination directory where the contents of the source folder will be copied
     *
     * @throws RuntimeException if any I/O error occurs during the copy process
     */
    public static void copyResourceFolder(final String sourceFolderInJar, final Path targetFolder) {

        final URL jarUrl = ResourcesUtils.class.getProtectionDomain().getCodeSource().getLocation();

        try (final FileSystem fs = newFileSystem(Path.of(jarUrl.toURI()), (ClassLoader) null)) {

            final Path sourceFolderInJarFS = fs.getPath("/" + sourceFolderInJar);

            try (final Stream<Path> filePaths = walk(sourceFolderInJarFS)) {

                for (final Path path : filePaths.toList()) {
                    final Path relativePath = sourceFolderInJarFS.relativize(path);
                    final Path targetPath   = targetFolder.resolve(relativePath.toString());

                    if (isDirectory(path)) {
                        createDirectory(targetPath);
                    }
                    else {
                        createDirectory(targetPath.getParent());
                        copy(path, targetPath, REPLACE_EXISTING);
                        log.info("Resource [{}] created", path);
                    }
                }
            }
        }
        catch (final Exception e) {
            log.error("Error processing source file: [{}] / target [{}]", sourceFolderInJar, targetFolder, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Copies a single resource from a specified sourceResourceName to a targetResourceName.
     *
     * @param baseTargetPath     The base path to start writing the resource to.
     * @param sourceResourceName The source of the resource with its name. Can be nested in folders.
     * @param targetResourceName The target resource with its name. Can be nested in folders. Can be different from the source
     */
    public static void copyResourceSingle(final Path baseTargetPath, final String sourceResourceName, final String targetResourceName) {

        try (final InputStream sourceStream = ResourcesUtils.class.getResourceAsStream("/" + sourceResourceName)) {

            if (sourceStream == null) {
                log.warn(formatBoldColor.apply(YELLOW, "Could not find [{}]"), sourceResourceName);
            }
            else {
                copy(sourceStream, baseTargetPath.resolve(targetResourceName), REPLACE_EXISTING);
                log.debug("Resource [{}] created", targetResourceName);
            }
        }
        catch (final Exception e) {
            log.error("Error copying resource: [{}] to [{}] ", sourceResourceName, targetResourceName, e);
            throw new RuntimeException(e);
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

    /**
     * Reads all lines from a specified file and returns them as a list of strings.
     * If the file does not exist, an empty list is returned.
     * In case of an I/O error, a {@link RuntimeException} is thrown.
     *
     * @param mavenConfigPath the path to the file to be read
     *
     * @return a list of strings containing all lines from the file, or an empty list if the file does not exist
     */
    public static List<String> readAllLinesFromFile(final Path mavenConfigPath) {

        try {
            return exists(mavenConfigPath) ? readAllLines(mavenConfigPath, UTF_8) : new ArrayList<>();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes all the provided lines to the specified file.
     * The file is created or truncated if it already exists.
     *
     * @param linesToWrite    the collection of lines to write to the file
     * @param mavenConfigPath the path to the file where the lines will be written
     */
    public static void writeAllLinesToFile(final Collection<String> linesToWrite, final Path mavenConfigPath) {

        try {
            log.debug(formatColor.apply(CYAN, "writing all lines [{}]"), linesToWrite);
            write(mavenConfigPath, linesToWrite, UTF_8, CREATE, TRUNCATE_EXISTING);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
