package ru.cft.service.argument;

import ru.cft.domain.ArgumentsNames;
import ru.cft.service.argument.exception.DuplicateArgumentNameException;

public interface ArgumentsNamesLoader {
    ArgumentsNames load() throws DuplicateArgumentNameException;
}
