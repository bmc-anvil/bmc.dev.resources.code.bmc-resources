package bmc.dev.resources.code.bmcresources.maven;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.support.InjectorResetForTest;
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

@Slf4j
class MavenConfigFileReaderTest extends InjectorResetForTest {

    @Test
    void getMavenPropertyValue_withErrorReadingFile_shouldThrow() {

        final MavenProject project              = createWithTestBaseDir();
        final String       expectedErrorMessage = "Simulated Error Message";
        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, "true");

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> exists(any())).thenThrow(new RuntimeException(new IOException(expectedErrorMessage)));

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMavenPropertyValue(PROP_COMPLETED_ARCH));
            assertInstanceOf(IOException.class, runtimeException.getCause().getCause());
            assertEquals(expectedErrorMessage, runtimeException.getCause().getCause().getMessage());
        }
    }

    @Test
    void getMavenPropertyValue_withFileDoesNotExist_shouldReturnEmpty() {

        final MavenProject project = createWithTestBaseDir();

        injectMavenProject(project);

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> exists(any())).thenReturn(false);

            final Optional<String> mavenPropertyValue = getMavenPropertyValue(PROP_COMPLETED_ARCH);

            assertTrue(mavenPropertyValue.isEmpty());
        }
    }

    @Test
    void getMavenPropertyValue_withInvalidProperty_shouldReturnEmpty() {

        final MavenProject project = createWithTestBaseDir();
        injectMavenProject(project);
        final String       expectedValue = "value";
        final List<String> lines         = List.of(PROP_COMPLETED_ARCH + "=" + expectedValue + "=should=work");

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> exists(any())).thenReturn(true);
            mockedFiles.when(() -> readAllLines(any(), any())).thenReturn(lines);

            final Optional<String> mavenPropertyValue = getMavenPropertyValue(PROP_COMPLETED_ARCH);

            assertFalse(mavenPropertyValue.isEmpty());
            assertEquals(expectedValue, mavenPropertyValue.get());
        }
    }

    @Test
    void getMavenPropertyValue_withPropertyDoesExist_shouldReturnTheValue() {

        final MavenProject project       = createWithTestBaseDir();
        final String       propertyValue = "true";

        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, propertyValue);

        final Optional<String> mavenPropertyResult = getMavenPropertyValue(PROP_COMPLETED_ARCH);

        assertTrue(mavenPropertyResult.isPresent());
        assertEquals(propertyValue, mavenPropertyResult.get());
    }

    @Test
    void getMavenPropertyValue_withPropertyDoesExists_withMalformedFormat_shouldThrow() {

        final MavenProject project              = createWithTestBaseDir();
        final String       expectedErrorMessage = "Simulated Error Message";
        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, "true");

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> exists(any())).thenReturn(true);
            mockedFiles.when(() -> readAllLines(any(), any())).thenThrow(new ArrayIndexOutOfBoundsException(expectedErrorMessage));

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMavenPropertyValue(PROP_COMPLETED_ARCH));

            assertEquals(runtimeException.getCause().getClass().getName() + ": " + expectedErrorMessage, runtimeException.getMessage());
        }
    }

    @Test
    void getMavenPropertyValue_withPropertyDoesNotExist_shouldReturnEmptyOptional() {

        final MavenProject project       = createWithTestBaseDir();
        final String       propertyValue = "true";

        injectMavenProject(project);
        writeMavenProperty(PROP_COMPLETED_ARCH, propertyValue);

        final Optional<String> mavenPropertyResult = getMavenPropertyValue("not.exists");

        assertTrue(mavenPropertyResult.isEmpty());
    }

}
