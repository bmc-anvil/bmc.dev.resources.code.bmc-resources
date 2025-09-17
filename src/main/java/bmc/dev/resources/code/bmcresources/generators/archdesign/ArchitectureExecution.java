package bmc.dev.resources.code.bmcresources.generators.archdesign;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.properties.ArchitectureConfig;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignStructureWriter.processArchitecture;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

public class ArchitectureExecution {

    public static void createArchitecture(final ArchitectureConfig architecture, final AbstractMojo mojo, final MavenProject mavenProject) {

        final Boolean archCompleted = ofNullable(getProperty(PROP_COMPLETED_ARCH)).map(Boolean::valueOf).orElse(FALSE);
        final Log     log           = mojo.getLog();

        if (architecture.isSkip() || archCompleted) {
            log.info("%sSkipping generation of architecture. Skip is [%s], Completed is [%s]%s"
                             .formatted(COLOR_GREEN, architecture.isSkip(), archCompleted, COLOR_RESET));
        }
        else {
            processArchitecture(mojo, mavenProject, architecture);
            writeMavenProperty(mojo, PROP_COMPLETED_ARCH, "true", mavenProject);
        }
    }

}
