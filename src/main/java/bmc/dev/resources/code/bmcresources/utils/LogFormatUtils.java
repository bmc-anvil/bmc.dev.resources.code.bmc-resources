package bmc.dev.resources.code.bmcresources.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

import lombok.experimental.UtilityClass;

import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.BOLD;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.RESET;

/**
 * This class controls formatting so logging in the code does not get bloated with colors and other formatting concerns.
 * <p>
 * This was an afternoon with some time to kill and not liking reading formatting instructions here and there on the code, plus some unsavory coloring on tests
 * due to Slf4j-simple binding.
 * <p>
 * What is life without whimsy?
 */
@UtilityClass
public class LogFormatUtils {

    /**
     * A {@link Function} that applies bold formatting to an input string by wrapping it with the ASCII escape codes.
     * <p>
     * The resulting string will appear bold when printed to a terminal that supports ANSI escape codes.
     */
    public static final Function<String, String>                   formatBold      = input -> BOLD.getAsciiCode() + input + RESET.getAsciiCode();
    /**
     * A {@link Function} that applies color formatting to an input string by wrapping it with the ASCII escape codes.
     * <p>
     * The resulting string will appear colored when printed to a terminal that supports ANSI escape codes.
     *
     * @see TerminalColors
     */
    public static final BiFunction<TerminalColors, String, String> formatColor     = (color, input) -> color.getAsciiCode() + input + RESET.getAsciiCode();
    /**
     * A {@link Function} that applies bold and color formatting to an input string by wrapping it with the ASCII escape codes.
     * <p>
     * The resulting string will appear colored when printed to a terminal that supports ANSI escape codes.
     *
     * @see TerminalColors
     */
    public static final BiFunction<TerminalColors, String, String> formatBoldColor = (color, input) -> BOLD.getAsciiCode() + formatColor.apply(color, input);

}
