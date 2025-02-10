package ru.cft.service.argument;

import org.apache.commons.cli.ParseException;
import ru.cft.domain.Arguments;

public interface ArgumentsParser {
    Arguments parse(String[] args) throws ParseException;
}
