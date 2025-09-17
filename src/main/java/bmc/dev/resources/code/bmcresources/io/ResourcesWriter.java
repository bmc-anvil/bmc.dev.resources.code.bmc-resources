package bmc.dev.resources.code.bmcresources.io;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ResourcesWriter {

    public static void writeResourceFile(final AbstractMojo mojo, final Path projectBasePath, final String source, final String target,
            final String padding) {

        final Log log = mojo.getLog();

        try (final InputStream sourceStream = mojo.getClass().getResourceAsStream("/" + source)) {
            final String sourcePadded = padding.formatted(source);

            if (sourceStream == null) {
                log.warn("Could not find [%s]".formatted(sourcePadded));
            }
            else {
                copy(sourceStream, projectBasePath.resolve(target), REPLACE_EXISTING);
                log.info("Resource [%s] created".formatted(sourcePadded));
            }
        }
        catch (final Exception e) {
            log.error("Error processing file: " + source, e);
            throw new RuntimeException(e);
        }
    }

    public static void writeResourceFolder(final AbstractMojo mojo, final String sourceFolderInJar, final Path targetFolder) {

        final URL jarUrl = mojo.getClass().getProtectionDomain().getCodeSource().getLocation();
        final Log log    = mojo.getLog();

        try (final FileSystem fs = newFileSystem(Path.of(jarUrl.toURI()), (ClassLoader) null)) {

            final Path sourceFolderInJarFS = fs.getPath("/" + sourceFolderInJar);

            try (final Stream<Path> filePaths = walk(sourceFolderInJarFS)) {

                for (final Path path : filePaths.toList()) {
                    final Path relativePath = sourceFolderInJarFS.relativize(path);
                    final Path targetPath   = targetFolder.resolve(relativePath.toString());

                    if (isDirectory(path)) {
                        createDirectory(mojo, targetPath);
                    }
                    else {
                        createDirectory(mojo, targetPath.getParent());
                        copy(path, targetPath, REPLACE_EXISTING);
                        log.info("Resource [%s] created".formatted(path));
                    }
                }
            }
        }
        catch (final Exception e) {
            log.error("Error processing source file: [%s] / target [%s]".formatted(sourceFolderInJar, targetFolder), e);
            throw new RuntimeException(e);
        }
    }

}


