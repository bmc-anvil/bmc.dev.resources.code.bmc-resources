package bmc.dev.resources.code.bmcresources.generators.resources;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesWriter.writeResourceFile;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesWriter.writeResourceFolder;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileUtilities.calculatePadding;

public class ResourcesUtils {

    public static void processResourcesMap(final AbstractMojo mojoClass, final MavenProject mavenProject,
            final Map.Entry<Integer, Map<String, String>> resourcesMap) {

        final Log log = mojoClass.getLog();

        resourcesMap.getValue().forEach((source, target) -> {

            final String padding              = calculatePadding.apply(resourcesMap.getKey());
            final Path   projectBasePath      = mavenProject.getBasedir().toPath();
            final Path   sourcePathAsResource = Path.of(Objects.requireNonNull(mojoClass.getClass().getResource("/" + source)).getPath());

            log.debug("Writing resource " + sourcePathAsResource);

            if (source.endsWith("/")) {
                log.debug("Directory: [%s]".formatted(sourcePathAsResource));

                writeResourceFolder(mojoClass, source, Path.of(target));
            }
            else {
                log.debug("File: [%s]".formatted(sourcePathAsResource));
                writeResourceFile(mojoClass, projectBasePath, source, target, padding);
            }
        });
    }

}
