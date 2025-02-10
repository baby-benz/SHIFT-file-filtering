package ru.cft.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class OsUtil {
    private static final String OS_NAME = System.getProperty("os.name");
    private static final boolean IS_OS_WINDOWS = isOSNameMatch("Windows");
    private static final boolean IS_OS_UNIX = isOSNameMatch("AIX") || isOSNameMatch("HP-UX")
            || isOSNameMatch("Irix") || isOSNameMatch("Linux") || isOSNameMatch("LINUX")
            || isOSNameMatch("Mac OS X") || isOSNameMatch("Solaris") || isOSNameMatch("SunOS")
            || isOSNameMatch("FreeBSD") || isOSNameMatch("OpenBSD") || isOSNameMatch("NetBSD");

    public static boolean isWindows() {
        return IS_OS_WINDOWS;
    }

    public static boolean isUnix() {
        return IS_OS_UNIX;
    }

    public static boolean isValidFilename(String fileName) {
        if (isWindows()) {
            return isValidForWindows(fileName);
        } else if (isUnix()) {
            return isValidForUNIX(fileName);
        }
        return true;
    }

    private static boolean isValidForWindows(String fileName) {
        if (fileName.charAt(fileName.length() - 1) == '.') return false;

        for (int i = 0; i < fileName.length(); i++) {
            char ch = fileName.charAt(i);
            if (ch < ' ' || "<>:\"|?*/".indexOf(ch) != -1) return false;
        }

        return true;
    }

    private static boolean isValidForUNIX(String fileName) {
        return !fileName.contains("/");
    }

    private static boolean isOSNameMatch(final String osNamePrefix) {
        if (OS_NAME == null) {
            return false;
        }
        return OS_NAME.startsWith(osNamePrefix);
    }
}
