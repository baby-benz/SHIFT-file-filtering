package ru.cft.service.argument;

import ru.cft.domain.ArgumentsNames;
import ru.cft.service.argument.exception.DuplicateArgumentNameException;
import ru.cft.util.PropertiesUtil;

import java.util.*;

public class PropertiesArgumentsNamesLoader implements ArgumentsNamesLoader {
    private static final String ARGUMENT_NAMES_PROPERTY_PREFIX = "cli.argument.names";

    private static final String PREFIX_PROPERTY_NAME = "prefix";
    private static final String OUTPUT_PROPERTY_NAME = "output";
    private static final String APPEND_PROPERTY_NAME = "append";
    private static final String STATISTICS_SIMPLE_PROPERTY_NAME = "statistics.simple";
    private static final String STATISTICS_FULL_PROPERTY_NAME = "statistics.full";

    private static final String LONG_PROPERTY_NAME = "long";
    private static final String SHORT_PROPERTY_NAME = "short";

    private static final String SHORT_PREFIX_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + PREFIX_PROPERTY_NAME + "." + SHORT_PROPERTY_NAME;
    private static final String LONG_PREFIX_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + PREFIX_PROPERTY_NAME + "." + LONG_PROPERTY_NAME;
    private static final String SHORT_OUTPUT_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + OUTPUT_PROPERTY_NAME + "." + SHORT_PROPERTY_NAME;
    private static final String LONG_OUTPUT_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + OUTPUT_PROPERTY_NAME + "." + LONG_PROPERTY_NAME;
    private static final String SHORT_APPEND_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + APPEND_PROPERTY_NAME + "." + SHORT_PROPERTY_NAME;
    private static final String LONG_APPEND_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + APPEND_PROPERTY_NAME + "." + LONG_PROPERTY_NAME;
    private static final String SHORT_SIMPLE_STATISTICS_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + STATISTICS_SIMPLE_PROPERTY_NAME + "." + SHORT_PROPERTY_NAME;
    private static final String LONG_SIMPLE_STATISTICS_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + STATISTICS_SIMPLE_PROPERTY_NAME + "." + LONG_PROPERTY_NAME;
    private static final String SHORT_FULL_STATISTICS_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + STATISTICS_FULL_PROPERTY_NAME + "." + SHORT_PROPERTY_NAME;
    private static final String LONG_FULL_STATISTICS_PROPERTY_NAME = ARGUMENT_NAMES_PROPERTY_PREFIX + "." + STATISTICS_FULL_PROPERTY_NAME + "." + LONG_PROPERTY_NAME;

    private ArgumentsNames argumentNames;

    @Override
    public ArgumentsNames load() throws DuplicateArgumentNameException {
        if (argumentNames == null) {
            loadArgumentNames();
        }
        return argumentNames;
    }

    private void loadArgumentNames() throws DuplicateArgumentNameException {
        var argsMet = new HashSet<String>();

        Map<String, String> argumentNames = PropertiesUtil.getProperties(ARGUMENT_NAMES_PROPERTY_PREFIX);

        for (String argumentName : argumentNames.values()) {
            if (!argsMet.add(argumentName)) {
                throw new DuplicateArgumentNameException(argumentName);
            }
        }

        this.argumentNames = buildArgumentNames(argumentNames);
    }

    private ArgumentsNames buildArgumentNames(Map<String, String> argumentNames) {
        String prefixShortName = argumentNames.get(SHORT_PREFIX_PROPERTY_NAME);
        String prefixLongName = argumentNames.get(LONG_PREFIX_PROPERTY_NAME);
        String outputShortName = argumentNames.get(SHORT_OUTPUT_PROPERTY_NAME);
        String outputLongName = argumentNames.get(LONG_OUTPUT_PROPERTY_NAME);
        String appendShortName = argumentNames.get(SHORT_APPEND_PROPERTY_NAME);
        String appendLongName = argumentNames.get(LONG_APPEND_PROPERTY_NAME);
        String simpleStatisticsShortName = argumentNames.get(SHORT_SIMPLE_STATISTICS_PROPERTY_NAME);
        String simpleStatisticsLongName = argumentNames.get(LONG_SIMPLE_STATISTICS_PROPERTY_NAME);
        String fullStatisticsShortName = argumentNames.get(SHORT_FULL_STATISTICS_PROPERTY_NAME);
        String fullStatisticsLongName = argumentNames.get(LONG_FULL_STATISTICS_PROPERTY_NAME);

        return new ArgumentsNames(
                new ArgumentsNames.ArgumentName(prefixShortName, prefixLongName),
                new ArgumentsNames.ArgumentName(outputShortName, outputLongName),
                new ArgumentsNames.ArgumentName(appendShortName, appendLongName),
                new ArgumentsNames.ArgumentName(simpleStatisticsShortName, simpleStatisticsLongName),
                new ArgumentsNames.ArgumentName(fullStatisticsShortName, fullStatisticsLongName)
        );
    }
}
