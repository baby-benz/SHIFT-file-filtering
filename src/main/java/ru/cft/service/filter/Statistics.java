package ru.cft.service.filter;

import ru.cft.domain.StatisticsType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

public record Statistics(long written, Optional<Integer> longestStringWritten, Optional<Integer> shortestStringWritten,
                  Optional<BigDecimal> maxNumber, Optional<BigDecimal> minNumber, Optional<BigDecimal> sum,
                  Optional<BigDecimal> avg) {
    public String toString(StatisticsType statisticsType) {
        if (statisticsType == null) {
            return this.toString();
        }

        String lineSeparator = System.lineSeparator();

        var sb = new StringBuilder("Статистика [").append(lineSeparator);

        if (statisticsType.equals(StatisticsType.SIMPLE)) {
            sb.append("\tЭлементов записано ").append(written).append(lineSeparator);
        } else if (statisticsType.equals(StatisticsType.FULL)) {
            sb.append("\tЭлементов записано ").append(written).append(lineSeparator);

            longestStringWritten.ifPresent(
                    i -> sb.append("\tСимволов в самой длинной обработанной строке ").append(i).append(lineSeparator)
            );
            shortestStringWritten.ifPresent(
                    i -> sb.append("\tСимволов в самой короткой обработанной строке ").append(i).append(lineSeparator)
            );
            maxNumber.ifPresent(
                    i -> sb.append("\tМаксимальное обработанное число ").append(i).append(lineSeparator)
            );
            minNumber.ifPresent(
                    i -> sb.append("\tМинимальное обработанное число ").append(i).append(lineSeparator)
            );
            sum.ifPresent(
                    i -> sb.append("\tСумма обработанных чисел ").append(format(i, 2)).append(lineSeparator)
            );
            avg.ifPresent(
                    i -> sb.append("\tСреднее обработанных чисел ").append(format(i, 2)).append(lineSeparator)
            );
        }

        return sb.append("]").toString();
    }

    private String format(BigDecimal number, int numberOfDecimalPlaces) {
        var format = new DecimalFormat("#." + "0".repeat(Math.max(0, numberOfDecimalPlaces)));
        return format.format(number);
    }
}
