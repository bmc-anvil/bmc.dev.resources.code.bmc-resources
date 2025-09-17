package bmc.dev.resources.code.bmcresources.utils;

public class StringUtils {

    public static boolean isNullOrBlank(final String stringToEvaluate) {

        return stringToEvaluate == null || stringToEvaluate.isBlank();
    }

}
