package com.blackmorse.joption.utils;

import com.blackmorse.joption.SingleOption;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public final class ArgParserUtils {
    private ArgParserUtils(){}

    public static Optional<String> parseStandartOneArg(SingleOption option, Scanner scanner) {
        Log.info("Argument: %s", option.getLongName() + " (" + option.getShortName() + ")");
        Log.log("Description: " + option.getDescription());
        Log.info("Type: %s", option.isRequired() ? "Required" : "Optional");
        if (option.getDefaultValue() != null) {
            Log.info("Default value: %s", String.valueOf(option.getDefaultValue()));
        }
        System.out.print("Enter value: ");
        String next = scanner.nextLine();
        if (option.isRequired() && StringUtils.isEmpty(next)) {
            Log.err("Required argument must be specified");
            return parseStandartOneArg(option, scanner);
        }
        return StringUtils.isEmpty(next) ?
                Optional.ofNullable(option.getDefaultValue() == null ? null : String.valueOf(option.getDefaultValue())) :
                Optional.of(next);
    }

    public static boolean parseStandardNoArgs(SingleOption option, Scanner scanner) {
        Log.info("Argument: %s", option.getLongName() + " (" + option.getShortName() + ")");
        Log.info("Description: " + option.getDescription());
        if (!option.isRequired()) {
            Log.info("Default value: %s", "false");
        }
        System.out.print("Y/N: ");
        String next = scanner.nextLine();
        if (option.isRequired() && StringUtils.isEmpty(next)) {
            Log.err("Required argument must be specified");
            return parseStandardNoArgs(option, scanner);
        }
        if (!option.isRequired() && StringUtils.isEmpty(next)) {
            Log.info("Using default value: %s", "false");
            return false;
        }
        if (!"N".equalsIgnoreCase(next) && !"Y".equalsIgnoreCase(next)) {
            Log.err("Unknown value " + next);
            return parseStandardNoArgs(option, scanner);
        }
        return "Y".equalsIgnoreCase(next);
    }

    public static Optional<List<String>> parseStandardMultiplyArgs(SingleOption option, Scanner scanner) {
        Log.info("Argument: %s", option.getLongName() + " (" + option.getShortName() + ")");
        Log.info("Description: " + option.getDescription());
        Log.info("%s", option.isRequired() ? "Required" : "Optional");
        if (option.getDefaultValue() != null) {
            Log.info("Default value: %s", String.valueOf(option.getDefaultValue()));
        }
        Log.log("Argument supports multiply values. To stop input just enter empty value");

        List<String> result = new ArrayList<>();

        String next = scanner.nextLine();
        while (!StringUtils.isEmpty(next)) {
            result.add(next);
            Log.info("Now value is: %s", result.toString());
            System.out.print("Enter next value: ");
            next = scanner.nextLine();
        }
        if (option.isRequired() && result.isEmpty()) {
            Log.info("Required parameter must be specified");
            return parseStandardMultiplyArgs(option, scanner);
        }
        return result.isEmpty() ? Optional.ofNullable((List<String>) option.getDefaultValue()) :
                Optional.of(result);
    }
}
