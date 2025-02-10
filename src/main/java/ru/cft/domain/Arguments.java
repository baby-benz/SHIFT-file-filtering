package ru.cft.domain;

import java.util.List;

public record Arguments(WriteMode writeMode, String fileNamePrefix, String outputDirectory,
                        StatisticsType statisticsType, List<String> filePathList) {
}
