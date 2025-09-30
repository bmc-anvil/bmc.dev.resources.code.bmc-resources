package bmc.dev.resources.code.bmcresources.utils;

import lombok.Getter;

/**
 * ASCII Colors supported by all or most Linux terminals.
 */
@Getter
public enum TerminalColors {

    BLUE("\u001B[34m"),
    BOLD("\u001B[1m"),
    CYAN("\u001B[36m"),
    GREEN("\u001B[32m"),
    RED("\u001B[31m"),
    RESET("\u001B[0m"),
    YELLOW("\u001B[33m");

    private final String asciiCode;

    TerminalColors(final String asciiCode) {

        this.asciiCode = asciiCode;

    }
}
