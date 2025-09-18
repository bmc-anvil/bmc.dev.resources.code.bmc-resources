package bmc.dev.resources.code.bmcresources.maven;

import org.apache.maven.project.MavenProject;

import lombok.extern.slf4j.Slf4j;

/**
 * Although there is some DI in maven 3.9.x, it is not as convenient as what I have seen coming for maven 4.x.x.
 * <p>
 * While waiting for maven 4 to be released, I'll use this type of trick to inject components.
 */
@Slf4j
public class MavenProjectInjector {

    private static MavenProject mavenProject;

    private MavenProjectInjector() {}

    /**
     * Retrieves the current instance of the MavenProject.
     * <p>
     * If the MavenProject has not been set, this method logs an error and
     * throws an IllegalStateException.
     * <p>
     * As there is only a given mavenProject in this project, no precautions have to be made for any concurrency concern.
     *
     * @return the initialized MavenProject instance
     *
     * @throws IllegalStateException if the MavenProject has not been set
     */
    public static MavenProject GetMavenProject() {

        if (mavenProject == null) {
            log.error("mavenProject is null, set it before before usage");
            throw new IllegalStateException("mavenProject is null, set it before before usage");
        }

        return mavenProject;
    }

    public static void setMavenProject(final MavenProject mavenProject) {

        MavenProjectInjector.mavenProject = mavenProject;
    }

}
