package bmc.dev.resources.code.bmcresources.generators.resources;

import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_RESOURCE;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesProcessor.processResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.GREEN;

@Slf4j
@UtilityClass
public class ResourcesExecution {

    public static void createResources(final ResourcesConfig resources) {

        if (resources.isSkip()) {
            log.info(formatColor.apply(GREEN, "{}Skipping generation of resources. Skip is [{}]{}"), true);
        }
        else {
            processResources(resources);
            writeMavenProperty(PROP_COMPLETED_RESOURCE, "true");
        }
    }

}
