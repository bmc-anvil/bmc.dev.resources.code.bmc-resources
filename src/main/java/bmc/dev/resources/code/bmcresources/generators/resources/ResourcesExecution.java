package bmc.dev.resources.code.bmcresources.generators.resources;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.properties.ResourcesConfig;
import bmc.dev.resources.code.bmcresources.mojos.ArchResourcesGeneratorMojo;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesProcessor.processResources;

public class ResourcesExecution {

    public static void createResources(final ResourcesConfig resources, final ArchResourcesGeneratorMojo mojo, final MavenProject mavenProject) {

        final Log log = mojo.getLog();

        if (resources.isSkip()) {
            log.info("%sSkipping generation of resources. Skip is [%s]%s".formatted(COLOR_GREEN, true, COLOR_RESET));
        }
        else {
            processResources(mojo, mavenProject, resources);
            writeMavenProperty(mojo, PROP_COMPLETED_RESOURCE, "true", mavenProject);
        }
    }

}
