package ru.cft.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledOnOs(OS.WINDOWS)
public class WindowsOsUtilTest {
    @Test
    void isValidFilename_for_windows_true() {
        assertTrue(OsUtil.isValidFilename("file"));
        assertTrue(OsUtil.isValidFilename("prefix_file"));
        assertTrue(OsUtil.isValidFilename("_file"));
        assertTrue(OsUtil.isValidFilename(".file"));
        assertTrue(OsUtil.isValidFilename("COM1"));
    }

    @Test
    void isValidFilename_for_windows_false() {
        assertFalse(OsUtil.isValidFilename("file>name"));
        assertFalse(OsUtil.isValidFilename("|file"));
        assertFalse(OsUtil.isValidFilename("file/"));
        assertFalse(OsUtil.isValidFilename("?"));
        assertFalse(OsUtil.isValidFilename("file."));
    }
}
