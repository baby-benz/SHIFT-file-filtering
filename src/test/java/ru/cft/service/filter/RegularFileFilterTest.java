package ru.cft.service.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.cft.domain.Arguments;
import ru.cft.domain.StatisticsType;
import ru.cft.domain.WriteMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegularFileFilterTest {
    final String testFileName1 = "file1.txt";
    final String testFileName2 = "file2.txt";
    final String integersOutputFilename = "integers.txt";
    final String floatsOutputFilename = "floats.txt";
    final String stringsOutputFilename = "strings.txt";

    @Test
    void filter_correct(@TempDir Path tempDir) throws IOException {
        final String prefix = "p_";

        Path inputFile1 = tempDir.resolve(testFileName1);
        Path inputFile2 = tempDir.resolve(testFileName2);

        String file1Content = """
                Lorem ipsum dolor sit amet
                45
                Пример
                3.1415
                consectetur adipiscing
                -0.001
                -1E-8
                тестовое задание
                100500
                0757
                0x1EF""";
        String file2Content = """
                Нормальная форма числа с плавающей запятой
                Long
                123456789
                1e1""";

        Files.write(inputFile1, file1Content.lines().toList(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        Files.write(inputFile2, file2Content.lines().toList(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        var filter = new RegularFileFilter();
        Statistics statistics = filter.filter(new Arguments(
                WriteMode.APPEND,
                prefix,
                tempDir.toString(),
                StatisticsType.FULL,
                List.of(inputFile1.toString(), inputFile2.toString())
        ));

        assertEquals(15, statistics.written());
        assertEquals(42, statistics.longestStringWritten().get());
        assertEquals(4, statistics.shortestStringWritten().get());
        assertEquals(BigDecimal.valueOf(123456789), statistics.maxNumber().get());
        assertEquals(BigDecimal.valueOf(-0.001), statistics.minNumber().get());
        assertEquals(BigDecimal.valueOf(123558337.14049), statistics.sum().get().setScale(5, RoundingMode.DOWN));
        assertEquals(BigDecimal.valueOf(13728704.126722), statistics.avg().get().setScale(6, RoundingMode.DOWN));

        String integersContent = """
                45
                100500
                0757
                0x1EF
                123456789
                1e1""";
        String floatsContent = """
                3.1415
                -0.001
                -1E-8""";
        String stringsContent = """
                Lorem ipsum dolor sit amet
                Пример
                consectetur adipiscing
                тестовое задание
                Нормальная форма числа с плавающей запятой
                Long""";

        assertThat(tempDir.resolve(prefix + integersOutputFilename).toFile()).hasContent(integersContent);
        assertThat(tempDir.resolve(prefix + floatsOutputFilename).toFile()).hasContent(floatsContent);
        assertThat(tempDir.resolve(prefix + stringsOutputFilename).toFile()).hasContent(stringsContent);
    }

    @Test
    void filter_bad_prefix() {
        String prefix = "p?";

        var filter = new RegularFileFilter();
        IllegalArgumentException exception =assertThrows(
                IllegalArgumentException.class,
                () -> filter.filter(new Arguments(
                        WriteMode.APPEND,
                        prefix,
                        null,
                        StatisticsType.FULL,
                        List.of()
                ))
        );
        assertEquals("Filename prefix " + prefix + " is not a valid filename", exception.getMessage());
    }
}