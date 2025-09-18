package bmc.dev.resources.code.bmcresources.utils;

import java.util.function.Predicate;

public class StringUtils {

    public static Predicate<String> isNullOrBlank = stringToEvaluate -> stringToEvaluate == null || stringToEvaluate.isBlank();

    private StringUtils() {}

}
