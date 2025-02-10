package ru.cft.service.filter;

import ru.cft.domain.Arguments;
import ru.cft.util.OsUtil;
import ru.cft.util.PropertiesUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractFileFilter implements FileFilter {
    protected static final Path CURRENT_DIR = Paths.get(System.getProperty("user.dir"));

    protected static final String INTEGERS_DEFAULT_FILE_NAME = "integers.txt";
    protected static final String FLOATS_DEFAULT_FILE_NAME = "floats.txt";
    protected static final String STRINGS_DEFAULT_FILE_NAME = "strings.txt";

    protected static final String INTEGERS_FILE_NAME = PropertiesUtil.getProperty(
            "cli.output.filenames.integer", INTEGERS_DEFAULT_FILE_NAME
    );
    protected static final String FLOATS_FILE_NAME = PropertiesUtil.getProperty(
            "cli.output.filenames.floating-point", FLOATS_DEFAULT_FILE_NAME
    );
    protected static final String STRINGS_FILE_NAME = PropertiesUtil.getProperty(
            "cli.output.filenames.string", STRINGS_DEFAULT_FILE_NAME
    );

    static {
        if (!OsUtil.isValidFilename(INTEGERS_FILE_NAME)) {
            System.err.println("INTEGERS filename contains illegal characters. Fix it in program settings");
            System.exit(0);
        }
        if (!OsUtil.isValidFilename(FLOATS_FILE_NAME)) {
            System.err.println("FLOATS filename contains illegal characters. Fix it in program settings");
            System.exit(0);
        }
        if (!OsUtil.isValidFilename(STRINGS_FILE_NAME)) {
            System.err.println("STRINGS filename contains illegal characters. Fix it in program settings");
            System.exit(0);
        }
    }

    public abstract Statistics filter(Arguments arguments);
}
