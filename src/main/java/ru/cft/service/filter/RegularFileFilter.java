package ru.cft.service.filter;

import ru.cft.domain.Arguments;
import ru.cft.domain.WriteMode;
import ru.cft.util.NumericUtil;
import ru.cft.util.OsUtil;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.*;
import java.util.Optional;

public class RegularFileFilter extends AbstractFileFilter {
    @Override
    public Statistics filter(Arguments arguments) {
        Path outputDirectory;
        String intsFileName = INTEGERS_FILE_NAME;
        String floatsFileName = FLOATS_FILE_NAME;
        String stringsFileName = STRINGS_FILE_NAME;

        if (arguments.fileNamePrefix() != null) {
            intsFileName = arguments.fileNamePrefix() + INTEGERS_FILE_NAME;
            if (!OsUtil.isValidFilename(intsFileName)) {
                throw new IllegalArgumentException("Filename prefix " + arguments.fileNamePrefix() + " is not a valid filename");
            }
            floatsFileName = arguments.fileNamePrefix() + FLOATS_FILE_NAME;
            stringsFileName = arguments.fileNamePrefix() + STRINGS_FILE_NAME;
        }

        if (arguments.outputDirectory() != null && !arguments.outputDirectory().isEmpty()) {
            try {
                outputDirectory = Path.of(arguments.outputDirectory());
                Files.createDirectories(outputDirectory);
            } catch (InvalidPathException e) {
                System.out.println("Bad output path: " + arguments.outputDirectory() + ". Using default output directory");
                outputDirectory = CURRENT_DIR;
            } catch (IOException e) {
                System.out.println("Error creating output directories: " + arguments.outputDirectory() + ". Using default output directory");
                outputDirectory = CURRENT_DIR;
            }
        } else {
            outputDirectory = CURRENT_DIR;
        }

        Path pathToIntsFile = outputDirectory.resolve(intsFileName);
        Path pathToFloatsFile = outputDirectory.resolve(floatsFileName);
        Path pathToStringsFile = outputDirectory.resolve(stringsFileName);

        BufferedWriter intsWriter = null, floatsWriter = null, stringsWriter = null;

        long writtenElementsCount = 0, writtenNumbersCount = 0;
        Integer longestWrittenString = null, shortestWrittenString = null;
        BigDecimal maxNumber = null, minNumber = null;
        BigDecimal sum = BigDecimal.valueOf(0);

        for (String filePath : arguments.filePathList()) {
            Path pathToInputFile;
            try {
                pathToInputFile = Path.of(filePath);
            } catch (InvalidPathException e) {
                System.err.println("Wrong input file path: " + filePath + ". Skipping");
                continue;
            }

            StandardOpenOption writeMode = arguments.writeMode().equals(WriteMode.APPEND)
                    ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING;

            try (BufferedReader in = Files.newBufferedReader(pathToInputFile)) {
                while (in.ready()) {
                    String line = in.readLine();
                    switch (NumericUtil.getNumberType(line)) {
                        case INTEGER -> {
                            try {
                                if (intsWriter == null) {
                                    intsWriter = createWriter(pathToIntsFile, StandardOpenOption.CREATE, writeMode);
                                }
                            } catch (IOException e) {
                                System.err.println("Error opening INTs file for writing " + e.getMessage());
                            }
                            if (intsWriter != null) {
                                writeToFile(intsWriter, line);
                                var writtenNumber = new BigDecimal(line);
                                if (maxNumber == null || writtenNumber.compareTo(maxNumber) > 0) {
                                    maxNumber = writtenNumber;
                                }
                                if (minNumber == null || writtenNumber.compareTo(minNumber) < 0) {
                                    minNumber = writtenNumber;
                                }
                                sum = sum.add(writtenNumber);
                                writtenNumbersCount++;
                                writtenElementsCount++;
                            }
                        }
                        case HEX_INTEGER -> {
                            try {
                                if (intsWriter == null) {
                                    intsWriter = createWriter(pathToIntsFile, StandardOpenOption.CREATE, writeMode);
                                }
                            } catch (IOException e) {
                                System.err.println("Error opening INTs file for writing " + e.getMessage());
                            }
                            if (intsWriter != null) {
                                writeToFile(intsWriter, line);
                                BigInteger writtenNumber = new BigInteger(line.substring(2), 16);
                                BigDecimal bigDecimalValue = new BigDecimal(writtenNumber);
                                if (maxNumber == null || bigDecimalValue.compareTo(maxNumber) > 0) {
                                    maxNumber = bigDecimalValue;
                                }
                                if (minNumber == null || bigDecimalValue.compareTo(minNumber) < 0) {
                                    minNumber = bigDecimalValue;
                                }
                                sum = sum.add(bigDecimalValue);
                                writtenNumbersCount++;
                                writtenElementsCount++;
                            }
                        }
                        case OCTAL_INTEGER -> {
                            try {
                                if (intsWriter == null) {
                                    intsWriter = createWriter(pathToIntsFile, StandardOpenOption.CREATE, writeMode);
                                }
                            } catch (IOException e) {
                                System.err.println("Error opening INTs file for writing " + e.getMessage());
                            }
                            if (intsWriter != null) {
                                writeToFile(intsWriter, line);
                                BigInteger writtenNumber = new BigInteger(line, 8);
                                BigDecimal bigDecimalValue = new BigDecimal(writtenNumber);
                                if (maxNumber == null || bigDecimalValue.compareTo(maxNumber) > 0) {
                                    maxNumber = bigDecimalValue;
                                }
                                if (minNumber == null || bigDecimalValue.compareTo(minNumber) < 0) {
                                    minNumber = bigDecimalValue;
                                }
                                sum = sum.add(bigDecimalValue);
                                writtenNumbersCount++;
                                writtenElementsCount++;
                            }
                        }
                        case FLOATING_POINT -> {
                            try {
                                if (floatsWriter == null) {
                                    floatsWriter = createWriter(pathToFloatsFile, StandardOpenOption.CREATE, writeMode);
                                }
                            } catch (IOException e) {
                                System.err.println("Error opening FLOATs file for writing " + e.getMessage());
                            }
                            if (floatsWriter != null) {
                                writeToFile(floatsWriter, line);
                                var writtenNumber = new BigDecimal(line);
                                if (maxNumber == null || writtenNumber.compareTo(maxNumber) > 0) {
                                    maxNumber = writtenNumber;
                                }
                                if (minNumber == null || writtenNumber.compareTo(minNumber) < 0) {
                                    minNumber = writtenNumber;
                                }
                                sum = sum.add(writtenNumber);
                                writtenNumbersCount++;
                                writtenElementsCount++;
                            }
                        }
                        case NOT_A_NUMBER -> {
                            try {
                                if (stringsWriter == null) {
                                    stringsWriter = createWriter(pathToStringsFile, StandardOpenOption.CREATE, writeMode);
                                }
                            } catch (IOException e) {
                                System.err.println("Error opening STRINGs file for writing " + e.getMessage());
                            }
                            if (stringsWriter != null) {
                                writeToFile(stringsWriter, line);
                                if (longestWrittenString == null || line.length() > longestWrittenString) {
                                    longestWrittenString = line.length();
                                }
                                if (shortestWrittenString == null || line.length() < shortestWrittenString) {
                                    shortestWrittenString = line.length();
                                }
                                writtenElementsCount++;
                            }
                        }
                    }

                }
            } catch (IOException e) {
                System.err.println("Error reading input file " + pathToInputFile);
            }
        }

        if (intsWriter != null) {
            try {
                intsWriter.close();
            } catch (IOException e) {
                System.err.println("Error closing INTs file");
            }
        }
        if (floatsWriter != null) {
            try {
                floatsWriter.close();
            } catch (IOException e) {
                System.err.println("Error closing FLOATs file");
            }
        }
        if (stringsWriter != null) {
            try {
                stringsWriter.close();
            } catch (IOException e) {
                System.err.println("Error closing STRINGs file");
            }
        }

        BigDecimal avg = writtenNumbersCount > 0
                ? sum.divide(BigDecimal.valueOf(writtenNumbersCount), RoundingMode.HALF_UP)
                : null;

        return new Statistics(
                writtenElementsCount, Optional.ofNullable(longestWrittenString), Optional.ofNullable(shortestWrittenString),
                Optional.ofNullable(maxNumber), Optional.ofNullable(minNumber), Optional.of(sum),
                Optional.ofNullable(avg)
        );
    }

    private BufferedWriter createWriter(Path path, OpenOption... options) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path, options)));
    }

    private void writeToFile(BufferedWriter writer, String line) throws IOException {
        if (writer == null) return;

        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error processing line: " + line);
        }
    }
}
