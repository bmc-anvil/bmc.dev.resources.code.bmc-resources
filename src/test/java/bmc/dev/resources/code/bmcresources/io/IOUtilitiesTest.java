package bmc.dev.resources.code.bmcresources.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.support.InjectorResetForTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.*;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.injectMavenProject;
import static bmc.dev.resources.code.support.ConstantsForTest.*;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@Slf4j
class IOUtilitiesTest extends InjectorResetForTest {

    @Test
    void copyResourceFile_withNoExistingSourceFile_noFileShouldBeCopied() {

        final MavenProject project        = createWithTestBaseDir();
        final Path         basePath       = project.getBasedir().toPath();
        final String       fileToCopy     = "i_do_not_exist.xml";
        final String       targetFileName = "test-pom-copy.xml";
        final Path         copiedFile     = basePath.resolve(targetFileName);

        copyResourceFile(basePath, fileToCopy, targetFileName);

        assertFalse(exists(copiedFile));
    }

    @Test
    void copyResourceFolder_withExistingJar_shouldCopy() {
        /*
         * In time there is the possibility that the jar changes to add some other testing.
         * That can break the names of sources we test here.
         * Consider opening the jar and confirming that the source folder and file exist as a 1st troubleshooting step.
         */
        final MavenProject project                 = createWithTestBaseDir();
        final Path         basePath                = project.getBasedir().toPath();
        final URL          jarUrl                  = this.getClass().getResource("/" + TEST_JAR_NAME);
        final String       sourceFileInFolderInJar = "workflow.yml";
        final Path         targetFolder            = basePath.resolve(SOURCE_FOLDER_IN_JAR);
        injectMavenProject(project);

        copyResourceFolder(jarUrl, SOURCE_FOLDER_IN_JAR, targetFolder);

        assertTrue(exists(targetFolder));
        assertTrue(targetFolder.toFile().isDirectory());

        assertTrue(exists(targetFolder.resolve(sourceFileInFolderInJar)));
        assertTrue(targetFolder.resolve(sourceFileInFolderInJar).toFile().isFile());
    }

    @Test
    void copyResourceFolder_withNonExistingJar_shouldThrowNPE() {
        /*
         * In time there is the possibility that the jar changes to add some other testing.
         * That can break the names of sources we test here.
         * Consider opening the jar and confirming that the source folder and file exist as a 1st troubleshooting step.
         */
        final MavenProject project      = createWithTestBaseDir();
        final Path         basePath     = project.getBasedir().toPath();
        final Path         targetFolder = basePath.resolve(SOURCE_FOLDER_IN_JAR);
        injectMavenProject(project);

        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> copyResourceFolder(null, SOURCE_FOLDER_IN_JAR, targetFolder));
        assertInstanceOf(NullPointerException.class, runtimeException.getCause());
    }

    @Test
    void copyResourceFolder_withNonExistingSourceFolderInJar_shouldThrowNoFileException() {
        /*
         * In time there is the possibility that the jar changes to add some other testing.
         * That can break the names of sources we test here.
         * Consider opening the jar and confirming that the source folder and file exist as a 1st troubleshooting step.
         */
        final MavenProject project      = createWithTestBaseDir();
        final Path         basePath     = project.getBasedir().toPath();
        final URL          jarUrl       = this.getClass().getResource("/" + TEST_JAR_NAME);
        final Path         targetFolder = basePath.resolve(SOURCE_FOLDER_NOT_IN_JAR);
        injectMavenProject(project);

        final RuntimeException runtimeException =
                assertThrows(RuntimeException.class, () -> copyResourceFolder(jarUrl, SOURCE_FOLDER_NOT_IN_JAR, targetFolder));
        assertInstanceOf(NoSuchFileException.class, runtimeException.getCause());
    }

    @Test
    void copyResourceFolder_withNonExistingTargetFolder_shouldThrowNPE() {
        /*
         * In time there is the possibility that the jar changes to add some other testing.
         * That can break the names of sources we test here.
         * Consider opening the jar and confirming that the source folder and file exist as a 1st troubleshooting step.
         */
        final URL          jarUrl       = this.getClass().getResource("/" + TEST_JAR_NAME);
        final Path         targetFolder = null;
        final MavenProject project      = createWithTestBaseDir();
        injectMavenProject(project);

        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> copyResourceFolder(jarUrl, SOURCE_FOLDER_IN_JAR, targetFolder));
        assertInstanceOf(NullPointerException.class, runtimeException.getCause());
    }

    @SneakyThrows
    @Test
    void copyResourceSingle_withExceptionTryingToCopy_shouldThrowAnException_noFileShouldBeCopied() {

        final MavenProject project        = createWithTestBaseDir();
        final Path         basePath       = project.getBasedir().toPath();
        final String       targetFileName = "test-pom-copy.xml";
        final String       fileToCopy     = "test-pom.xml";
        final Path         copiedFile     = basePath.resolve(targetFileName);

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any())).thenThrow(IOException.class);

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> copyResourceFile(basePath, fileToCopy, targetFileName));
            assertInstanceOf(IOException.class, runtimeException.getCause());
        }

        assertFalse(exists(copiedFile));
    }

    @Test
    void copyResourceSingle_withExistingSourceFile_shouldCopyTheResource() {

        final MavenProject project        = createWithTestBaseDir();
        final Path         basePath       = project.getBasedir().toPath();
        final String       fileToCopy     = "test-pom.xml";
        final String       targetFileName = "test-pom-copy.xml";
        final Path         copiedFile     = basePath.resolve(targetFileName);

        copyResourceFile(basePath, fileToCopy, targetFileName);

        assertTrue(exists(copiedFile));
    }

    @Test
    void createDirectory_shouldCreateDirectoriesSafely() {

        final MavenProject project    = createWithTestBaseDir();
        final Path         basePath   = project.getBasedir().toPath();
        final Path         targetPath = basePath.resolve("target", "to", "create");

        createDirectoriesSafely(targetPath);

        assertTrue(exists(targetPath));
        assertTrue(isDirectory(targetPath));
    }

    @Test
    void createDirectory_withException_shouldThrowAnException_andShouldNotCreateADirectory() {

        final MavenProject project    = createWithTestBaseDir();
        final Path         basePath   = project.getBasedir().toPath();
        final Path         targetPath = basePath.resolve("target", "to", "create");

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenThrow(IOException.class);

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> createDirectoriesSafely(targetPath));
            assertInstanceOf(IOException.class, runtimeException.getCause());
        }

        assertFalse(exists(targetPath));
        assertFalse(isDirectory(targetPath));
    }

    @Test
    void readAllLinesFromFile_withException_shouldThrowAnException() {

        final MavenProject project    = createWithTestBaseDir();
        final Path         basePath   = project.getBasedir().toPath();
        final String       fileToRead = "test-pom.xml";

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> exists(any(Path.class))).thenReturn(TRUE);
            mockedFiles.when(() -> Files.readAllLines(any(Path.class), eq(UTF_8))).thenThrow(IOException.class);

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> readAllLinesFromFile(basePath.resolve(fileToRead)));

            assertInstanceOf(IOException.class, runtimeException.getCause());
        }
    }

    @Test
    void readAllLinesFromFile_withExistingFile_shouldReturnFileLinesAsList() {

        final MavenProject project           = createWithTestBaseDir();
        final Path         basePath          = project.getBasedir().toPath();
        final String       fileToRead        = "test-pom.xml";
        final List<String> someExpectedLines = List.of("project", "modelVersion", "groupId", "version", TEST_POM_PROP_KEY, TEST_POM_PROP_VALUE);

        final List<String> linesFromFile = readAllLinesFromFile(basePath.resolve(fileToRead));

        assertEquals(TEST_POM_TOTAL_LINES, linesFromFile.size());

        someExpectedLines.forEach(someExpectedLine -> {
            log.info("asserting the retrieved lines from file contain [{}]", someExpectedLine);
            assertTrue(linesFromFile.stream().anyMatch(line -> line.contains(someExpectedLine)));
        });
    }

    @Test
    void readAllLinesFromFile_withNonExistingFile_shouldReturnEmptyList() {

        final MavenProject project    = createWithTestBaseDir();
        final Path         basePath   = project.getBasedir().toPath();
        final String       fileToRead = "i_do_not_exist.xml";

        final List<String> linesFromFile = readAllLinesFromFile(basePath.resolve(fileToRead));

        assertTrue(linesFromFile.isEmpty());
    }

    @SneakyThrows
    @Test
    void writeAllLinesToFile_shouldCreateFileWithAllLines() {

        final MavenProject project      = createWithTestBaseDir();
        final String       fileName     = "writeAllLinesFile.config";
        final Path         basePath     = project.getBasedir().toPath();
        final Path         targetPath   = basePath.resolve(fileName);
        final List<String> linesToWrite = List.of("these", "lines", "should", "be", "on", "the", "file");

        assertFalse(exists(targetPath));

        writeAllLinesToFile(linesToWrite, targetPath);

        assertTrue(exists(targetPath));
        final List<String> linesFromFile = readAllLines(targetPath);
        assertEquals(linesToWrite.size(), linesFromFile.size());

        linesToWrite.forEach(expectedLine -> {
            log.info("asserting the retrieved lines from match [{}]", expectedLine);
            assertTrue(linesFromFile.stream().anyMatch(line -> line.contains(expectedLine)));
        });

    }

    @SneakyThrows
    @Test
    void writeAllLinesToFile_withException_shouldThrowException() {

        final MavenProject project      = createWithTestBaseDir();
        final String       fileName     = "writeAllLinesFile.config";
        final Path         basePath     = project.getBasedir().toPath();
        final Path         targetPath   = basePath.resolve(fileName);
        final List<String> linesToWrite = List.of("these", "lines", "should", "be", "on", "the", "file");

        assertFalse(exists(targetPath));

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> write(any(Path.class), any(), eq(UTF_8), eq(CREATE), eq(TRUNCATE_EXISTING))).thenThrow(IOException.class);

            final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> writeAllLinesToFile(linesToWrite, targetPath));

            assertInstanceOf(IOException.class, runtimeException.getCause());
        }
    }

}
