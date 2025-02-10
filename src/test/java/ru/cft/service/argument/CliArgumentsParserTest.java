package ru.cft.service.argument;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.*;
import ru.cft.domain.Arguments;
import ru.cft.domain.ArgumentsNames;
import ru.cft.domain.StatisticsType;
import ru.cft.domain.WriteMode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CliArgumentsParserTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final CliArgumentsParser parser = new CliArgumentsParser(new ArgumentsNames(
            new ArgumentsNames.ArgumentName("p", "prefix"),
            new ArgumentsNames.ArgumentName("o", "output"),
            new ArgumentsNames.ArgumentName("a", "append"),
            new ArgumentsNames.ArgumentName("s", "simple"),
            new ArgumentsNames.ArgumentName("f", "full")
    ));

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void parse() throws ParseException {
        Arguments args1 = parser.parse(new String[]{"-p", "p_", "-o", "output", "-a", "-simple", "some_file.file"});
        assertEquals(
                new Arguments(
                        WriteMode.APPEND,
                        "p_",
                        "output",
                        StatisticsType.SIMPLE,
                        List.of("some_file.file")
                ),
                args1
        );

        Arguments args2 = parser.parse(new String[]{"-p", "p_", "-o", "output", "some_file.file"});
        assertEquals(
                new Arguments(
                        WriteMode.OVERWRITE,
                        "p_",
                        "output",
                        StatisticsType.NONE,
                        List.of("some_file.file")
                ),
                args2
        );

        Arguments args3 = parser.parse(new String[]{"-p", "p_", "-o", "output", "some_file.file", "-f"});
        assertEquals(
                new Arguments(
                        WriteMode.OVERWRITE,
                        "p_",
                        "output",
                        StatisticsType.FULL,
                        List.of("some_file.file")
                ),
                args3
        );
    }

    @Test
    void parse_empty_file_list() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(new String[]{"-p", "p_", "-o", "output", "-a", "-simple"})
        );
        assertEquals("Empty file list was passed as arguments", exception.getMessage());
    }

    @Test
    void parse_duplicate_statistics_option() throws ParseException {
        Arguments args = parser.parse(new String[]{"-p", "p_", "-o", "output", "-a", "-simple", "-f", "some_file.file"});

        assertEquals(
                new Arguments(
                        WriteMode.APPEND,
                        "p_",
                        "output",
                        StatisticsType.FULL,
                        List.of("some_file.file")
                ),
                args
        );

        String consoleOutput = outContent.toString();
        assertEquals(
                "Duplicate statistics option was provided. Using full statistics mode",
                consoleOutput.substring(0, consoleOutput.length() - 2)
        );
    }
}