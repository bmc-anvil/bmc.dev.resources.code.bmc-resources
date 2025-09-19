package bmc.dev.resources.code.bmcresources.generators.resources;

import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_RESOURCE;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesProcessor.processResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatGreen;

@Slf4j
public class ResourcesExecution {

    public static void createResources(final ResourcesConfig resources) {

        if (resources.isSkip()) {
            log.info(formatGreen("{}Skipping generation of resources. Skip is [{}]{}"), true);
        }
        else {
            processResources(resources);
            writeMavenProperty(PROP_COMPLETED_RESOURCE, "true");
        }
    }

}
