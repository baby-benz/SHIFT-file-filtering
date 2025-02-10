package ru.cft.service.filter;

import ru.cft.domain.Arguments;

public interface FileFilter {
    Statistics filter(Arguments arguments);
}
