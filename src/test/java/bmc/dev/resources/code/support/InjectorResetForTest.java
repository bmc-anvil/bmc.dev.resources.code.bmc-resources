package bmc.dev.resources.code.support;

import org.junit.jupiter.api.AfterEach;

import bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector;

public class InjectorResetForTest {

    @AfterEach
    void reset() {

        MavenProjectInjector.reset();
    }

}
