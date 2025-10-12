package bmc.dev.resources.code.bmcresources.maven;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.*;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.junit.jupiter.api.Assertions.*;

class MavenProjectInjectorTest extends InjectorResetForTest {

    @Test
    void getMavenProject_withProjectIsNotSet_throwsIllegalStateException() {

        assertThrows(IllegalStateException.class, MavenProjectInjector::getMavenProject);
    }

    @Test
    void getMavenProject_withProjectIsSetAndReset_returnsOriginalMavenProject() {

        final MavenProject mavenProject  = createWithTestBaseDir();
        final MavenProject mavenProject2 = createWithTestBaseDir();

        injectMavenProject(mavenProject);
        assertSame(mavenProject, getMavenProject());

        injectMavenProject(mavenProject2);
        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void getMavenProject_withProjectIsSet_returnsMavenProject() {

        final MavenProject mavenProject = createWithTestBaseDir();
        injectMavenProject(mavenProject);

        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void injectMavenProject_withProjectIsAlreadySet_returnsFalse() {

        final MavenProject mavenProject    = createWithTestBaseDir();
        final MavenProject mavenProject2   = createWithTestBaseDir();
        final boolean      injectionResult = injectMavenProject(mavenProject);

        assertTrue(injectionResult);
        assertSame(mavenProject, getMavenProject());

        final boolean injectionResultOnSecondSet = injectMavenProject(mavenProject2);
        assertFalse(injectionResultOnSecondSet);
    }

    @Test
    void injectMavenProject_withProjectIsNotNull_returnsTrue() {

        final MavenProject mavenProject    = createWithTestBaseDir();
        final boolean      injectionResult = injectMavenProject(mavenProject);

        assertTrue(injectionResult);
        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void injectMavenProject_withProjectIsNull_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> injectMavenProject(null));
    }

    @Test
    void reset_withProjectIsSet_returnsNullMavenProject() {

        final MavenProject mavenProject = createWithTestBaseDir();
        injectMavenProject(mavenProject);
        reset();

        assertThrows(IllegalStateException.class, MavenProjectInjector::getMavenProject);
    }

}
