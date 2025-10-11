package bmc.dev.resources.code.bmcresources.maven;

import java.nio.file.Files;
import java.util.Optional;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import bmc.dev.resources.code.support.InjectorResetForTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_ARCH;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileReader.getMavenPropertyValue;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.injectMavenProject;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MavenConfigFileReaderTest extends InjectorResetForTest {

    @Test
    void fileDoesNotExist_returnsEmptyOptional() {

        final MavenProject project = createWithTestBaseDir();

        injectMavenProject(project);

        final Optional<String> mavenPropertyResult = getMavenPropertyValue("not.exists");

        assertTrue(mavenPropertyResult.isEmpty());
    }

    @Test
    void propertyDoesExist_returnsTheValue() {

        final MavenProject project       = createWithTestBaseDir();
        final String       propertyValue = "true";

        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, propertyValue);

        final Optional<String> mavenPropertyResult = getMavenPropertyValue(PROP_COMPLETED_ARCH);

        assertTrue(mavenPropertyResult.isPresent());
        assertEquals(propertyValue, mavenPropertyResult.get());
    }

    @SneakyThrows
    @Test
    void propertyDoesExists_malformedFormat_throws() {

        final MavenProject project              = createWithTestBaseDir();
        final String       expectedErrorMessage = "Simulated Error Message";
        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, "true");

        /*
         * NOTE for the future:
         * When mocking static, all methods of the mocked class are intercepted and will return whatever default for the return type.
         * In this case Files.exists() will always return false, unless a when instruction configures it to do otherwise.
         */
        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> exists(any())).thenReturn(true);
            mockedFiles.when(() -> readAllLines(any(), any())).thenThrow(new ArrayIndexOutOfBoundsException(expectedErrorMessage));

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMavenPropertyValue(PROP_COMPLETED_ARCH));

            assertEquals(runtimeException.getCause().getClass().getName() + ": " + expectedErrorMessage, runtimeException.getMessage());
        }
    }

    @Test
    void propertyDoesNotExist_returnsEmptyOptional() {

        final MavenProject project       = createWithTestBaseDir();
        final String       propertyValue = "true";

        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, propertyValue);

        final Optional<String> mavenPropertyResult = getMavenPropertyValue("not.exists");

        assertTrue(mavenPropertyResult.isEmpty());
    }

}
