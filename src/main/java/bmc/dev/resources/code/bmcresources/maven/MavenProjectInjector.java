package bmc.dev.resources.code.bmcresources.maven;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.maven.project.MavenProject;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatYellowBold;
import static java.util.Optional.ofNullable;

/**
 * Although there is some DI in maven 3.9.x, it is not as convenient as what I have seen coming for maven 4.x.x.
 * <p>
 * While waiting for maven 4 to be released, I'll use this type of trick to inject components.
 * <p>
 * To make this extra clear: this is a service-locator antipattern compromise that allows me to avoid using current DI or passing mavenProject around in calls.
 * Those two concerns are of higher importance than passing a mavenProject around; one of the very few that have a persistent (in-mem) state in the whole
 * plugin.
 * <p>
 * It can be awkward in tests as we need to manually set it up. With a proper DI framework, with an annotation, we would have it done for us.
 */
@Slf4j
public class MavenProjectInjector {

    private static final AtomicReference<MavenProject> ATOMIC_MAVEN_PROJECT           = new AtomicReference<>();
    private static final String                        ERROR_MAVEN_PROJECT_NOT_SET    = "mavenProject is null, set it before before usage";
    private static final String                        ERROR_MAVEN_PROJECT_PARAM_NULL = "mavenProject parameter cannot be null";

    private MavenProjectInjector() {}

    /**
     * Retrieves the current instance of the MavenProject.
     * <p>
     * If the MavenProject has not been set, this method throws an {@link IllegalStateException}.
     * <p>
     *
     * @return the initialized MavenProject instance
     *
     * @throws IllegalStateException if the MavenProject has not been set
     */
    public static MavenProject getMavenProject() {

        return ofNullable(ATOMIC_MAVEN_PROJECT.get())
                .orElseThrow(() -> new IllegalStateException(ERROR_MAVEN_PROJECT_NOT_SET));
    }

    /**
     * Resets the maven project by setting it to null.
     */
    public static void reset() {

        ATOMIC_MAVEN_PROJECT.set(null);
    }

    /**
     * Sets the MOJO's {@link MavenProject}.
     * <p>
     * Although this should be set only once with a single mavenProject, precautions are in place to avoid possible problems in the future.
     */
    public static boolean setMavenProject(final MavenProject mavenProject) {

        if (mavenProject == null) {
            throw new IllegalArgumentException(ERROR_MAVEN_PROJECT_PARAM_NULL);
        }

        final boolean isSet = ATOMIC_MAVEN_PROJECT.compareAndSet(null, mavenProject);

        if (!isSet) {
            log.warn(formatYellowBold("mavenProject is already initialized"));
        }

        return isSet;
    }

}
