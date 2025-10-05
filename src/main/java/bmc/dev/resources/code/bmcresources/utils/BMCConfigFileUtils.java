package bmc.dev.resources.code.bmcresources.utils;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import lombok.experimental.UtilityClass;

import static bmc.dev.resources.code.bmcresources.Constants.COMMENT_PREFIX;
import static bmc.dev.resources.code.bmcresources.Constants.STRUCTURE_SEPARATOR;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static java.util.Comparator.comparing;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Utility class providing functionality for processing and manipulating BMC resources configuration files.
 */
@UtilityClass
public class BMCConfigFileUtils {

    /**
     * A {@link Function} that generates a string format specifier for left-aligned padding
     * based on the given width.
     * <p>
     * The input integer represents the desired width of the formatted string, and the resulting
     * format specifier will be a left-aligned string placeholder (e.g., "%-10s" for width 10).
     */
    public static final  Function<Integer, String>                         calculateLeftAlignedPadding = length -> "%-" + length + "s";
    /**
     * A {@link Function} that takes a {@link String} and converts it into an {@link ArchitectureEntry}.
     * <p>
     * The function trims any leading or trailing whitespace from both parts of the split input string.
     */
    public static final  Function<String, ArchitectureEntry>               createArchitectureEntry     = line -> {
        final String[]         lineParts = line.split(STRUCTURE_SEPARATOR);
        final String           folder    = lineParts[0].trim();
        final Optional<String> readme    = lineParts.length > 1 ? of(lineParts[1].trim()) : empty();

        return new ArchitectureEntry(folder, readme);
    };
    /**
     * A {@link Function} that takes a {@link String} and converts it into an {@link ResourceEntry}.
     * <p>
     * The function trims any leading or trailing whitespace from both parts of the split input string.
     */
    public static final  Function<String, ResourceEntry>                   createResourceEntry         = line -> {
        final String[]         lineParts  = line.split(STRUCTURE_SEPARATOR);
        final String           source     = lineParts[0].trim();
        final String           target     = lineParts.length > 1 ? lineParts[1].trim() : "";
        final Optional<String> permission = lineParts.length > 2 ? of(lineParts[2].trim()) : empty();

        return new ResourceEntry(source, target, permission);
    };
    /**
     * A {@link Function} that computes the maximum string length from a given {@link Collection} of strings.
     */
    public static final  Function<Collection<String>, Integer>             getMaxStringLength          =
            strings -> strings.stream().map(String::length).max(Integer::compareTo).orElse(0);
    /**
     * A {@link Predicate} used to filter out from a collection of strings:
     * <br>- null / blank as evaluated by {@link StringUtils#isNullOrBlank}.
     * <br>- comment lines
     * <br>- non parseable lines (i.e. starting with a separator, etc...).
     * <p>
     * This predicate returns {@code true} for strings that are neither empty/blank nor comments, etc.
     */
    public static final  Predicate<String>                                 isLineProcessable           =
            input -> !(isNullOrBlank.test(input) || input.trim().startsWith(COMMENT_PREFIX) || input.trim().startsWith(STRUCTURE_SEPARATOR));
    /**
     * A static final comparator that compares instances of {@link ArchitectureEntry} based on their {@link ArchitectureEntry#folder()} values.
     */
    private static final Comparator<? super ArchitectureEntry>             comparingByFolder           = comparing(ArchitectureEntry::folder);
    /**
     * A {@link Function} that processes a {@link BufferedReader} to extract configuration data.
     * <p>
     * The input is expected to be read line by line, with each line potentially representing a {@link ArchitectureEntry}.
     * <p>
     * The result is a sorted, ordered List of {@link ArchitectureEntry}.
     */
    public static final  Function<BufferedReader, List<ArchitectureEntry>> extractArchitectureModel    = reader -> reader.lines()
                                                                                                                         .filter(isLineProcessable)
                                                                                                                         .map(createArchitectureEntry)
                                                                                                                         .sorted(comparingByFolder)
                                                                                                                         .toList();
    /**
     * A static final comparator that compares instances of {@link ResourceEntry} based on their {@link ResourceEntry#source()} values.
     */
    private static final Comparator<? super ResourceEntry>                 comparingBySource           = comparing(ResourceEntry::source);
    /**
     * A {@link Function} that processes a {@link BufferedReader} to extract configuration data.
     * <p>
     * The input is expected to be read line by line, with each line potentially representing a {@link ResourceEntry}.
     * <p>
     * The result is a sorted, ordered List of {@link ResourceEntry}.
     */
    public static final  Function<BufferedReader, List<ResourceEntry>>     extractResources            = reader -> reader.lines()
                                                                                                                         .filter(isLineProcessable)
                                                                                                                         .map(createResourceEntry)
                                                                                                                         .sorted(comparingBySource)
                                                                                                                         .toList();

}
