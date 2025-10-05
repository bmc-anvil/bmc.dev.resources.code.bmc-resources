package bmc.dev.resources.code.bmcresources.generators.resources;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFolder;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceSingle;
import static bmc.dev.resources.code.bmcresources.io.OSUtilities.makeFileExecutable;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static java.util.Objects.requireNonNull;

/**
 * Utility class for processing resources.
 */
@Slf4j
@UtilityClass
public class ResourcesUtils {

    public static final String EXECUTABLE_PERMISSION = "x";

    /**
     * This method exists for testability while keeping the class stateless.
     * <p>
     * A mockStatic can make it return any other Jar we need for testing, and given the supplier itself is final and set,
     * there is no risk of errors in productive code.
     *
     * @return the JarSupplier
     *
     * @see Supplier
     */
    public static URL getJarUrl() {

        return ResourcesUtils.class.getProtectionDomain()
                                   .getCodeSource()
                                   .getLocation();
    }

    /**
     * Processes a List of {@link ResourceEntry}.
     * <p>
     * The parsing takes care of directories and particular files.
     * <br>If a given file is marked as executable, it will make the copied target file executable.
     * <p>
     *
     * @param configEntries A list of configuration entries to process
     */
    public static void processResourcesMap(final List<ResourceEntry> configEntries) {

        final Path projectBasePath = getMavenProject().getBasedir().toPath();

        configEntries.forEach(configEntry -> {

            final String source               = configEntry.source();
            final String target               = configEntry.target();
            final Path   sourcePathAsResource = Path.of(requireNonNull(ResourcesUtils.class.getResource("/" + source)).getPath());

            log.debug(formatColor.apply(CYAN, "Writing resource [{}]"), sourcePathAsResource);

            if (source.endsWith("/")) {
                log.debug(formatColor.apply(CYAN, "Directory: [{}]"), sourcePathAsResource);
                copyResourceFolder(getJarUrl(), source, Path.of(target));
            } else {
                log.debug(formatColor.apply(CYAN, "File: [{}]"), sourcePathAsResource);
                copyResourceSingle(projectBasePath, source, target);
            }

            configEntry.permission()
                       .filter(EXECUTABLE_PERMISSION::equals)
                       .ifPresent(_ -> makeFileExecutable(configEntry.target()));
        });
    }

}
