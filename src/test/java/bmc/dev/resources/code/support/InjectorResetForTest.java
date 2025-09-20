package bmc.dev.resources.code.support;

import org.junit.jupiter.api.AfterEach;

import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.reset;

public class InjectorResetForTest {

    @AfterEach
    void resetInjection() {

        reset();
    }

}
