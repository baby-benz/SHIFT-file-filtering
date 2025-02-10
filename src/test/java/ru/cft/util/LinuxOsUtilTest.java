package ru.cft.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledOnOs({OS.LINUX, OS.MAC, OS.AIX, OS.FREEBSD, OS.OPENBSD, OS.SOLARIS})
public class LinuxOsUtilTest {
    @Test
    void isValidFilename_for_unix_true() {
        assertTrue(OsUtil.isValidFilename("file"));
        assertTrue(OsUtil.isValidFilename("prefix_file"));
        assertTrue(OsUtil.isValidFilename("_file"));
        assertTrue(OsUtil.isValidFilename(".file"));
        assertTrue(OsUtil.isValidFilename("COM1"));
        assertTrue(OsUtil.isValidFilename("fi>le"));
        assertTrue(OsUtil.isValidFilename("fi<le"));
        assertTrue(OsUtil.isValidFilename("fi:le"));
    }

    @Test
    void isValidFilename_for_unix_false() {
        assertFalse(OsUtil.isValidFilename("file/name"));
    }
}
