package bmc.dev.resources.code.bmcresources.io;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.stream.Stream;

import bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.YELLOW;
import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
public class ResourcesWriter {

    private ResourcesWriter() {}

    public static void writeResourceFile(final Path projectBasePath, final String source, final String target, final String padding) {

        try (final InputStream sourceStream = ResourcesUtils.class.getResourceAsStream("/" + source)) {
            final String sourcePadded = padding.formatted(source);

            if (sourceStream == null) {
                log.warn(formatBoldColor.apply(YELLOW, "Could not find [{}]"), sourcePadded);
            }
            else {
                copy(sourceStream, projectBasePath.resolve(target), REPLACE_EXISTING);
                log.info("Resource [{}] created", sourcePadded);
            }
        }
        catch (final Exception e) {
            log.error("Error processing file: {}", source, e);
            throw new RuntimeException(e);
        }
    }

    public static void writeResourceFolder(final String sourceFolderInJar, final Path targetFolder) {

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

}


