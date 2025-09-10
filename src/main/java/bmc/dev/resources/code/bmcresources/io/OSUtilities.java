package bmc.dev.resources.code.bmcresources.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;

import static java.nio.file.Files.getPosixFilePermissions;
import static java.nio.file.Files.setPosixFilePermissions;
import static java.nio.file.attribute.PosixFilePermission.*;

public class OSUtilities {

    /**
     * Modifies the file permissions to make files executable for the owner, group, and others.
     * <p>
     * A fallback mechanism is provided for non-POSIX compliant file systems, such as Windows, though I have not tested it.
     *
     * @param mojoClass       AbstractMojo class being passed for its utilities and loaders.
     * @param computedExecMap An entry consisting of an integer key and a map value, where the map's keys
     *                        represent file paths to be made executable, and the values are additional metadata.
     */
    public static void makeFilesExecutable(final AbstractMojo mojoClass, final Entry<Integer, Map<String, String>> computedExecMap) {

        final Log log = mojoClass.getLog();

        for (Entry<String, String> entry : computedExecMap.getValue().entrySet()) {
            final String fileToMakeExecutable     = entry.getKey();
            final String _                        = entry.getValue();
            final Path   fileToMakeExecutablePath = Paths.get(fileToMakeExecutable);

            try {

                final Set<PosixFilePermission> permissions = new HashSet<>(getPosixFilePermissions(fileToMakeExecutablePath));

                permissions.add(OWNER_EXECUTE);
                permissions.add(GROUP_EXECUTE);
                permissions.add(OTHERS_EXECUTE);
                setPosixFilePermissions(fileToMakeExecutablePath, permissions);
                log.info("file [%s] is now executable".formatted(fileToMakeExecutable));
            }

            catch (final UnsupportedOperationException e) {
                // Fallback for Windows maybe, cannot test it myself
                final boolean isPermissionSet = fileToMakeExecutablePath.toFile().setExecutable(true, false);
                if (!isPermissionSet) {
                    log.error("Could not set permission to execute to file [%s]".formatted(fileToMakeExecutable), e);
                }
            }
            catch (final Exception e) {
                log.error("Error changing file permissions for [%s]".formatted(fileToMakeExecutable), e);
                throw new RuntimeException(e);
            }
        }

    }

}
