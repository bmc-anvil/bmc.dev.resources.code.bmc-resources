package bmc.dev.resources.code.bmcresources.maven;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.*;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.junit.jupiter.api.Assertions.*;

class MavenProjectInjectorTest extends InjectorResetForTest {

    @Test
    void get_projectIsNotSet_throwsIllegalStateException() {

        assertThrows(IllegalStateException.class, MavenProjectInjector::getMavenProject);
    }

    @Test
    void get_projectIsSetAndReset_returnsOriginalMavenProject() {

        final MavenProject mavenProject  = createWithTestBaseDir();
        final MavenProject mavenProject2 = createWithTestBaseDir();

        setMavenProject(mavenProject);
        assertSame(mavenProject, getMavenProject());

        setMavenProject(mavenProject2);
        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void get_projectIsSet_returnsMavenProject() {

        final MavenProject mavenProject = createWithTestBaseDir();
        setMavenProject(mavenProject);

        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void reset_projectIsSet_returnsNullMavenProject() {

        final MavenProject mavenProject = createWithTestBaseDir();
        setMavenProject(mavenProject);
        reset();

        assertThrows(IllegalStateException.class, MavenProjectInjector::getMavenProject);
    }

    @Test
    void set_projectIsAlreadySet_returnsFalse() {

        final MavenProject mavenProject    = createWithTestBaseDir();
        final MavenProject mavenProject2   = createWithTestBaseDir();
        final boolean      injectionResult = setMavenProject(mavenProject);

        assertTrue(injectionResult);
        assertSame(mavenProject, getMavenProject());

        final boolean injectionResultOnSecondSet = setMavenProject(mavenProject2);
        assertFalse(injectionResultOnSecondSet);
    }

    @Test
    void set_projectIsNotNull_returnsTrue() {

        final MavenProject mavenProject    = createWithTestBaseDir();
        final boolean      injectionResult = setMavenProject(mavenProject);

        assertTrue(injectionResult);
        assertSame(mavenProject, getMavenProject());
    }

    @Test
    void set_projectIsNull_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> setMavenProject(null));
    }

}
