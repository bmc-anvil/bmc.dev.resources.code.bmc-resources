package bmc.dev.resources.code.bmcresources.generators.resources;

import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculatePadding;
import static bmc.dev.resources.code.bmcresources.io.ResourcesWriter.writeResourceFile;
import static bmc.dev.resources.code.bmcresources.io.ResourcesWriter.writeResourceFolder;
import static java.util.Objects.requireNonNull;

public class ResourcesUtils {

    public static void processResourcesMap(final AbstractMojo mojo, final MavenProject mavenProject,
            final Map.Entry<Integer, Map<String, String>> resourcesMap) {

        final Log log = mojo.getLog();

        resourcesMap.getValue().forEach((source, target) -> {

            final String padding              = calculatePadding.apply(resourcesMap.getKey());
            final Path   projectBasePath      = mavenProject.getBasedir().toPath();
            final Path   sourcePathAsResource = Path.of(requireNonNull(mojo.getClass().getResource("/" + source)).getPath());

            log.debug("Writing resource " + sourcePathAsResource);

            if (source.endsWith("/")) {
                log.debug("Directory: [%s]".formatted(sourcePathAsResource));

                writeResourceFolder(mojo, source, Path.of(target));
            }
            else {
                log.debug("File: [%s]".formatted(sourcePathAsResource));
                writeResourceFile(mojo, projectBasePath, source, target, padding);
            }
        });
    }

}
