package bmc.dev.resources.code.bmcresources.io;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.support.InjectorResetForTest;
import lombok.SneakyThrows;

import static bmc.dev.resources.code.bmcresources.io.OSUtilities.makeFileExecutable;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.getPosixFilePermissions;
import static java.nio.file.Files.write;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.attribute.PosixFilePermission.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * I believe that these tests, when run on a non-POSIX system, will fail all tests except for the one testing the non-POSIX fallback.
 * <p>
 * I am still to determine if to separate these tests into classes... or put a check on the OS and conditionally expect one or the other thing.
 * Maybe go full mocks and some integration tests with real files like below; it seems the benefit will create quasi duplicate for no benefit.
 * For a first drop of the plugin, targeting Unix-like systems should suffice.
 * <p>
 * TODO: NOTE: for a small article on how mocks and real objects can coexist and make for better testing than pure mocks.
 *       <br> Though it is true that the remark above will be mute on full mocks, using real objects and using mocks to control some
 *       execution paths gives me some peace of mind.
 *       <br>So where is the usefulness boundary between pure unitTests and some kind of "integration tests", oh naja...
 */
class OSUtilitiesTest extends InjectorResetForTest {

    @SneakyThrows
    @Test
    void makeFilesExecutable_withMockedNonPosixAndNotSettingPermissions_shouldNotChangeFiles() {

        final String       nothingToDoFileName   = "nothingToDoFileName.sh";
        final List<String> nothingToDoBashScript = of("#!/bin/bash");
        final Path         executableFilePath    = createWithTestBaseDir().getBasedir().toPath().resolve(nothingToDoFileName);
        final File         executableFile        = executableFilePath.toFile();
        final File         spiedExecutableFile   = spy(executableFile);

        doReturn(false).when(spiedExecutableFile).setExecutable(anyBoolean(), anyBoolean());

        write(executableFilePath, nothingToDoBashScript, UTF_8, CREATE, TRUNCATE_EXISTING);
        final Set<PosixFilePermission> originalFilePermissions = getPosixFilePermissions(executableFilePath);
        assertFalse(originalFilePermissions.contains(OWNER_EXECUTE));
        assertFalse(originalFilePermissions.contains(GROUP_EXECUTE));
        assertFalse(originalFilePermissions.contains(OTHERS_EXECUTE));

        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class)) {
            final Path mockPath = mock(Path.class);
            mockedPaths.when(() -> Paths.get(anyString())).thenReturn(mockPath);
            when(mockPath.toFile()).thenReturn(spiedExecutableFile);
            when(mockPath.getFileSystem()).thenThrow(UnsupportedOperationException.class);

            makeFileExecutable(executableFilePath.toString());
        }

        final Set<PosixFilePermission> updatedFilePermissions = getPosixFilePermissions(executableFilePath);
        assertFalse(updatedFilePermissions.contains(OWNER_EXECUTE));
        assertFalse(updatedFilePermissions.contains(GROUP_EXECUTE));
        assertFalse(updatedFilePermissions.contains(OTHERS_EXECUTE));
    }

    @SneakyThrows
    @Test
    void makeFilesExecutable_withExistingFile_shouldUpdatePermissions() {

        final String       nothingToDoFileName   = "nothingToDoFileName.sh";
        final List<String> nothingToDoBashScript = of("#!/bin/bash");
        final Path         executableFilePath    = createWithTestBaseDir().getBasedir().toPath().resolve(nothingToDoFileName);

        write(executableFilePath, nothingToDoBashScript, UTF_8, CREATE, TRUNCATE_EXISTING);
        final Set<PosixFilePermission> originalFilePermissions = getPosixFilePermissions(executableFilePath);
        assertFalse(originalFilePermissions.contains(OWNER_EXECUTE));
        assertFalse(originalFilePermissions.contains(GROUP_EXECUTE));
        assertFalse(originalFilePermissions.contains(OTHERS_EXECUTE));

        makeFileExecutable(executableFilePath.toString());
        final Set<PosixFilePermission> updatedFilePermissions = getPosixFilePermissions(executableFilePath);
        assertTrue(updatedFilePermissions.contains(OWNER_EXECUTE));
        assertTrue(updatedFilePermissions.contains(GROUP_EXECUTE));
        assertTrue(updatedFilePermissions.contains(OTHERS_EXECUTE));

    }

    @SneakyThrows
    @Test
    void makeFilesExecutable_withMockedNonPosix_shouldAttemptToMakeFileExecutableFallback() {

        final String       nothingToDoFileName   = "nothingToDoFileName.sh";
        final List<String> nothingToDoBashScript = of("#!/bin/bash");
        final Path         executableFilePath    = createWithTestBaseDir().getBasedir().toPath().resolve(nothingToDoFileName);
        write(executableFilePath, nothingToDoBashScript, UTF_8, CREATE, TRUNCATE_EXISTING);

        final Set<PosixFilePermission> originalFilePermissions = getPosixFilePermissions(executableFilePath);
        assertFalse(originalFilePermissions.contains(OWNER_EXECUTE));
        assertFalse(originalFilePermissions.contains(GROUP_EXECUTE));
        assertFalse(originalFilePermissions.contains(OTHERS_EXECUTE));

        try (final MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> getPosixFilePermissions(any(Path.class))).thenThrow(new UnsupportedOperationException("POSIX not supported"));

            makeFileExecutable(executableFilePath.toString());
        }

        final Set<PosixFilePermission> updatedFilePermissions = getPosixFilePermissions(executableFilePath);
        assertTrue(updatedFilePermissions.contains(OWNER_EXECUTE));
        assertTrue(updatedFilePermissions.contains(GROUP_EXECUTE));
        assertTrue(updatedFilePermissions.contains(OTHERS_EXECUTE));
    }

    @SneakyThrows
    @Test
    void makeFilesExecutable_withNonExistingFile_shouldThrowException() {

        final String noFileHere = createWithTestBaseDir().getBasedir().toPath() + "noFileHere.sh";

        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> makeFileExecutable(noFileHere));

        assertInstanceOf(NoSuchFileException.class, runtimeException.getCause());

    }

}
