package bmc.dev.resources.code.bmcresources.generators.resources;

import java.nio.file.Path;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.ResourcesWriter.writeResourceFile;
import static bmc.dev.resources.code.bmcresources.io.ResourcesWriter.writeResourceFolder;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculateLeftAlignedPadding;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static java.util.Objects.requireNonNull;

@Slf4j
public class ResourcesUtils {

    public static void processResourcesMap(final Map.Entry<Integer, Map<String, String>> resourcesMap) {

        resourcesMap.getValue().forEach((source, target) -> {

            final String padding              = calculateLeftAlignedPadding.apply(resourcesMap.getKey());
            final Path   projectBasePath      = getMavenProject().getBasedir().toPath();
            final Path   sourcePathAsResource = Path.of(requireNonNull(ResourcesUtils.class.getResource("/" + source)).getPath());

            log.debug(formatColor.apply(CYAN, "Writing resource [{}]"), sourcePathAsResource);

            if (source.endsWith("/")) {
                log.debug(formatColor.apply(CYAN, "Directory: [{}]"), sourcePathAsResource);

                writeResourceFolder(source, Path.of(target));
            }
            else {
                log.debug(formatColor.apply(CYAN, "File: [{}]"), sourcePathAsResource);
                writeResourceFile(projectBasePath, source, target, padding);
            }
        });
    }

}
