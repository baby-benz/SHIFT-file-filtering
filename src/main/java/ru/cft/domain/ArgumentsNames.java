package ru.cft.domain;

public record ArgumentsNames(ArgumentName prefix, ArgumentName output, ArgumentName append,
                             ArgumentName simpleStatistics, ArgumentName fullStatistics) {
    public record ArgumentName(String shortName, String longName) {
    }

    public ArgumentsNames(ArgumentName prefix, ArgumentName output, ArgumentName append,
                          ArgumentName simpleStatistics, ArgumentName fullStatistics) {
        this.prefix = fillArgumentName(prefix, "p", "prefix");
        this.output = fillArgumentName(output, "o", "output");
        this.append = fillArgumentName(append, "a", "append");
        this.simpleStatistics = fillArgumentName(simpleStatistics, "s", "simple");
        this.fullStatistics = fillArgumentName(fullStatistics, "f", "full");
    }

    private ArgumentName fillArgumentName(ArgumentName argumentName, String shortDefaultName, String longDefaultName) {
        if (argumentName == null
                || argumentName.shortName == null && argumentName.longName == null
                || (argumentName.shortName != null && argumentName.shortName.isEmpty()) && (argumentName.longName != null && argumentName.longName.isEmpty())) {
            return new ArgumentName(shortDefaultName, longDefaultName);
        } else if (argumentName.shortName == null || argumentName.shortName.isEmpty()) {
            return new ArgumentName(shortDefaultName, argumentName.longName);
        } else if (argumentName.longName.isEmpty()) {
            return new ArgumentName(argumentName.shortName, longDefaultName);
        }
        return new ArgumentName(argumentName.shortName, argumentName.longName);
    }
}
