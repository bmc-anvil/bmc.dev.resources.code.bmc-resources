package bmc.dev.resources.code.bmcresources.generators.resources;

import java.nio.file.Path;
import java.util.Map;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFolder;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceSingle;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static java.util.Objects.requireNonNull;

@Slf4j
@UtilityClass
public class ResourcesUtils {

    public static void processResourcesMap(final Map.Entry<Integer, Map<String, String>> resourcesMap) {

        resourcesMap.getValue().forEach((source, target) -> {

            final Path projectBasePath      = getMavenProject().getBasedir().toPath();
            final Path sourcePathAsResource = Path.of(requireNonNull(ResourcesUtils.class.getResource("/" + source)).getPath());

            log.debug(formatColor.apply(CYAN, "Writing resource [{}]"), sourcePathAsResource);

            if (source.endsWith("/")) {
                log.debug(formatColor.apply(CYAN, "Directory: [{}]"), sourcePathAsResource);

                copyResourceFolder(source, Path.of(target));
            }
            else {
                log.debug(formatColor.apply(CYAN, "File: [{}]"), sourcePathAsResource);
                copyResourceSingle(projectBasePath, source, target);
            }
        });
    }

}
