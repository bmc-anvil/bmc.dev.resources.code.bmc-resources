package bmc.dev.resources.code.bmcresources.maven;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.junit.jupiter.api.Assertions.*;

class MavenProjectInjectorTest extends InjectorResetForTest {

    @Test
    void testInjectsProject_projectIsAlreadySet_returnsFalse() {

        final MavenProject mavenProject    = createWithTestBaseDir();
        final boolean      injectionResult = setMavenProject(mavenProject);

        assertTrue(injectionResult);
        assertSame(mavenProject, getMavenProject());

        final boolean injectionResultOnSecondSet = setMavenProject(mavenProject);
        assertFalse(injectionResultOnSecondSet);
        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void testInjectsProject_projectIsNotNull_returnsTrue() {

        final MavenProject mavenProject    = createWithTestBaseDir();
        final boolean      injectionResult = setMavenProject(mavenProject);

        assertTrue(injectionResult);
        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void testInjectsProject_projectIsNull_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> setMavenProject(null));
    }

}
