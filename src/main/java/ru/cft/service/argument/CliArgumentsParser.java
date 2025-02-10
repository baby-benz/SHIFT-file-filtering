package ru.cft.service.argument;

import org.apache.commons.cli.*;
import ru.cft.domain.ArgumentsNames;
import ru.cft.domain.Arguments;
import ru.cft.domain.StatisticsType;
import ru.cft.domain.WriteMode;

public class CliArgumentsParser implements ArgumentsParser {
    private final CommandLineParser parser;
    private final Options options;
    private final Option appendOption;
    private final Option prefixOption;
    private final Option outputOption;
    private final Option simpleStatisticsOption;
    private final Option fullStatisticsOption;

    public CliArgumentsParser(ArgumentsNames argumentsNames) {
        this.appendOption = buildOption(argumentsNames.append(), false, false);
        this.prefixOption = buildOption(argumentsNames.prefix(), true, false);
        this.outputOption = buildOption(argumentsNames.output(), true, false);
        this.simpleStatisticsOption = buildOption(argumentsNames.simpleStatistics(), false, false);
        this.fullStatisticsOption = buildOption(argumentsNames.fullStatistics(), false, false);

        this.options = new Options()
                .addOption(appendOption)
                .addOption(prefixOption)
                .addOption(outputOption)
                .addOption(simpleStatisticsOption)
                .addOption(fullStatisticsOption);

        this.parser = new DefaultParser();
    }

    @Override
    public Arguments parse(String[] args) throws ParseException {
        Arguments arguments = parse(options, args);
        if (arguments.filePathList().isEmpty()) {
            throw new IllegalArgumentException("Empty file list was passed as arguments");
        }
        return arguments;
    }

    private Arguments parse(Options options, String[] args) throws ParseException {
        CommandLine commandLine = parser.parse(options, args);

        WriteMode writeMode = commandLine.hasOption(appendOption) ? WriteMode.APPEND : WriteMode.OVERWRITE;
        String filePrefix = commandLine.getOptionValue(prefixOption);
        String outputDirectory = commandLine.getOptionValue(outputOption);
        StatisticsType statisticsType;

        if (commandLine.hasOption(simpleStatisticsOption) && commandLine.hasOption(fullStatisticsOption)) {
            System.out.println("Duplicate statistics option was provided. Using full statistics mode");
            statisticsType = StatisticsType.FULL;
        } else if (commandLine.hasOption(simpleStatisticsOption)) {
            statisticsType = StatisticsType.SIMPLE;
        } else if (commandLine.hasOption(fullStatisticsOption)) {
            statisticsType = StatisticsType.FULL;
        } else {
            statisticsType = StatisticsType.NONE;
        }

        return new Arguments(writeMode, filePrefix, outputDirectory, statisticsType, commandLine.getArgList());
    }

    private Option buildOption(ArgumentsNames.ArgumentName argumentsName, boolean hasArg, boolean required) {
        return Option.builder(argumentsName.shortName())
                .longOpt(argumentsName.longName())
                .hasArg(hasArg)
                .required(required)
                .build();
    }
}
