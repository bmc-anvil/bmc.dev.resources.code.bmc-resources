package bmc.dev.resources.code.bmcresources.utils;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.*;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.BOLD;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.RESET;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class LogFormatUtilsTest {

    @Test
    void formatStringWithBoldColor_returnsExpectedMarkersInOutput() {

        for (final TerminalColors color : TerminalColors.values()) {
            if (color.equals(RESET) || color.equals(BOLD)) {
                continue;
            }

            final String output          = formatBoldColor.apply(color, "test input");
            final String outputMinusBold = output.substring(BOLD.getAsciiCode().length());

            assertTrue(output.startsWith(BOLD.getAsciiCode()));
            assertTrue(output.endsWith(RESET.getAsciiCode()));

            assertTrue(outputMinusBold.startsWith(color.getAsciiCode()));
            assertTrue(outputMinusBold.endsWith(RESET.getAsciiCode()));

            log.info("testing output with bold colors: [{}]", output);
        }
    }

    @Test
    void formatStringWithBoldFormatOnly_returnsExpectedMarkersInOutput() {

        for (final TerminalColors color : TerminalColors.values()) {
            if (color.equals(RESET) || color.equals(BOLD)) {
                continue;
            }

            final String output = formatBold.apply("test input");

            assertTrue(output.startsWith(BOLD.getAsciiCode()));
            assertTrue(output.endsWith(RESET.getAsciiCode()));

            log.info("testing output with bold format - No Colors: [{}]", output);
        }
    }

    @Test
    void formatStringWithColor_returnsExpectedMarkersInOutput() {

        for (final TerminalColors color : TerminalColors.values()) {
            if (color.equals(RESET) || color.equals(BOLD)) {
                continue;
            }

            final String output = formatColor.apply(color, "test input");

            assertTrue(output.startsWith(color.getAsciiCode()));
            assertTrue(output.endsWith(RESET.getAsciiCode()));

            log.info("testing output with colors: [{}]", output);
        }
    }

}
