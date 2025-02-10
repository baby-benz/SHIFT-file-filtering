package ru.cft;

import ru.cft.domain.Arguments;
import ru.cft.domain.ArgumentsNames;
import ru.cft.domain.StatisticsType;
import ru.cft.service.argument.ArgumentsNamesLoader;
import ru.cft.service.argument.ArgumentsParser;
import ru.cft.service.argument.CliArgumentsParser;
import ru.cft.service.argument.PropertiesArgumentsNamesLoader;
import ru.cft.service.filter.FileFilter;
import ru.cft.service.filter.RegularFileFilter;
import ru.cft.service.filter.Statistics;

public class Main {
    public static void main(String[] args) {
        try {
            ArgumentsNamesLoader argumentsNamesLoader = new PropertiesArgumentsNamesLoader();
            ArgumentsNames argumentsNames = argumentsNamesLoader.load();
            ArgumentsParser argumentsParser = new CliArgumentsParser(argumentsNames);
            Arguments arguments = argumentsParser.parse(args);
                FileFilter filter = new RegularFileFilter();
            Statistics statistics = filter.filter(arguments);
            if (!arguments.statisticsType().equals(StatisticsType.NONE)) {
                System.out.println(statistics.toString(arguments.statisticsType()));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}