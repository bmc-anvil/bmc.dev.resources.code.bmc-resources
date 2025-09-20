package bmc.dev.resources.code.bmcresources.maven;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileUtils.findPropertyIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MavenConfigFileUtilsTest {

    private static final String WALDO = "Waldo";

    @Test
    void listDoesContainsProperty_returnsExpectedPosition() {

        final List<String> waldoList = List.of("where", "is", WALDO);
        final OptionalInt  result    = findPropertyIndex.apply(waldoList, WALDO);

        assertTrue(result.isPresent());
        assertEquals(2, result.getAsInt());
    }

    @Test
    void listDoesNotContainsProperty_returnsOptionalEmpty() {

        final List<String> noWaldoList = List.of("where", "is");
        final OptionalInt  result      = findPropertyIndex.apply(noWaldoList, WALDO);

        assertTrue(result.isEmpty());
    }

    @Test
    void listIsEmpty_returnsOptionalEmpty() {

        final ArrayList<String> emptyList = new ArrayList<>();
        final OptionalInt       result    = findPropertyIndex.apply(emptyList, WALDO);

        assertTrue(result.isEmpty());
    }

}
