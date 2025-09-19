package bmc.dev.resources.code.bmcresources.utils;

/**
 * This class controls formatting so logging in the code does not get bloated with colors and other formatting concerns.
 * <p>
 * This was an afternoon with some time to kill and not liking reading formatting instructions here and there on the code, plus some unsavory coloring on tests
 * due to Slf4j-simple binding.
 * <p>
 * What is life without whimsy?
 */
public class LogFormattingUtils {

    public static final String COLOR_BLUE   = "\u001B[34m";
    public static final String COLOR_BOLD   = "\u001B[1m";
    public static final String COLOR_CYAN   = "\u001B[36m";
    public static final String COLOR_GREEN  = "\u001B[32m";
    public static final String COLOR_RED    = "\u001B[31m";
    public static final String COLOR_RESET  = "\u001B[0m";
    public static final String COLOR_YELLOW = "\u001B[33m";

    public static String formatBlue(final String message) {

        return COLOR_BLUE + message + COLOR_RESET;
    }

    public static String formatBlueBold(final String message) {

        return COLOR_BOLD + formatBlue(message);
    }

    public static String formatCyan(final String message) {

        return COLOR_CYAN + message + COLOR_RESET;
    }

    public static String formatGreen(final String message) {

        return COLOR_GREEN + message + COLOR_RESET;
    }

    public static String formatRed(final String message) {

        return COLOR_RED + message + COLOR_RESET;
    }

    public static String formatRedBold(final String message) {

        return COLOR_BOLD + formatRed(message);
    }

    public static String formatYellow(final String message) {

        return COLOR_YELLOW + message + COLOR_RESET;
    }

    public static String formatYellowBold(final String message) {

        return COLOR_BOLD + formatYellow(message);
    }

}
