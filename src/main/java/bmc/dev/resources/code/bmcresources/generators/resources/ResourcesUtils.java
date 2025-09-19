package bmc.dev.resources.code.bmcresources.generators.resources;

import java.nio.file.Path;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.ResourcesWriter.writeResourceFile;
import static bmc.dev.resources.code.bmcresources.io.ResourcesWriter.writeResourceFolder;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculatePadding;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatCyan;
import static java.util.Objects.requireNonNull;

@Slf4j
public class ResourcesUtils {

    public static void processResourcesMap(final Map.Entry<Integer, Map<String, String>> resourcesMap) {

        resourcesMap.getValue().forEach((source, target) -> {

            final String padding              = calculatePadding.apply(resourcesMap.getKey());
            final Path   projectBasePath      = getMavenProject().getBasedir().toPath();
            final Path   sourcePathAsResource = Path.of(requireNonNull(ResourcesUtils.class.getResource("/" + source)).getPath());

            log.debug(formatCyan("Writing resource [{}]"), sourcePathAsResource);

            if (source.endsWith("/")) {
                log.debug(formatCyan("Directory: [{}]"), sourcePathAsResource);

                writeResourceFolder(source, Path.of(target));
            }
            else {
                log.debug(formatCyan("File: [{}]"), sourcePathAsResource);
                writeResourceFile(projectBasePath, source, target, padding);
            }
        });
    }

}
