package com.blackmorse.joption.group;

import com.blackmorse.joption.SingleOption;
import com.blackmorse.joption.exceptions.CommandValidationException;
import com.blackmorse.joption.utils.Log;
import com.blackmorse.joption.utils.OptionsConverter;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SelectOneGroup implements Group {
    private final String name;
    private final List<SingleOption> options;

    public SelectOneGroup(String name, List<SingleOption> options) {
        this.name = name;
        this.options = options;
    }

    @Override
    public void readData(Scanner scanner, Map<String, Object> result) {
        Log.info("Parameters %s are in group " + name, options.stream().map(SingleOption::getLongName).collect(Collectors.joining(", ")));
        Log.info("Exactly one of them must be specified");
        Optional opt = null;
        for (SingleOption option : options) {
            if (option.getArgsNum().equals(SingleOption.ARG_NUM.NO_ARG)) {
                opt = noArg(option, scanner);
            } else if (option.getArgsNum().equals(SingleOption.ARG_NUM.ONE_ARG)) {
                opt = oneArg(option, scanner);
            } else {
                opt = multiplyArgs(option, scanner);
            }
            Log.log("_______________________________");
            Objects.requireNonNull(opt);
            if (opt.isPresent()) {
                result.put(option.getLongName(), opt.get());
                break;
            }
        }

        if (!opt.isPresent()) {
            Log.err("No one of group parameters is specified");
            readData(scanner, result);
        }
    }

    @Override
    public List<Option> getCliOptions() {
        return options.stream()
                .map(OptionsConverter::convertToCliOption)
                .collect(Collectors.toList());
    }

    @Override
    public List<SingleOption> getSingleOptions() {
        return options;
    }

    @Override
    public void checkGroup(Map<String, Object> valuesMap) {
        String valueName = null;
        for (SingleOption option : options) {
            if (valuesMap.get(option.getLongName()) != null) {
                if (valueName != null) {
                    throw new CommandValidationException("Two ore more parameters specified in group " + name +
                            ": " + valueName + ", " + valuesMap.get(option.getLongName()));
                } else {
                    valueName = option.getLongName();
                }
            }
        }
        if (valueName == null) {
            throw new CommandValidationException("No parameters specified in group " +name);
        }
    }

    private Optional<Boolean> noArg(SingleOption option, Scanner scanner) {
        Log.info("Argument: %s" , option.getLongName() + " (" + option.getShortName() + ")");
        Log.info("Description: " + option.getDescription());
        System.out.print("Y/N: ");
        String next = scanner.nextLine();
        if (StringUtils.isEmpty(next)) {
            return Optional.empty();
        }
        if (!"N".equalsIgnoreCase(next) && !"Y".equalsIgnoreCase(next)) {
            Log.err("Unknown value " + next);
            return noArg(option, scanner);
        }
        return Optional.of("Y".equalsIgnoreCase(next));
    }

    private Optional<String> oneArg(SingleOption option, Scanner scanner) {
        Log.info("Argument: %s" , option.getLongName() + " (" + option.getShortName() + ")");
        Log.log("Description: " + option.getDescription());
        System.out.print("Enter value: ");
        String next = scanner.nextLine();
        if (StringUtils.isEmpty(next)) {
            return Optional.empty();
        }
        return Optional.of(next);
    }

    private Optional<List<String>> multiplyArgs(SingleOption option, Scanner scanner) {
        Log.info("Argument: %s" , option.getLongName() + " (" + option.getShortName() + ")");
        Log.info("Description: " + option.getDescription());
        Log.log("Argument supports multiply values. To stop input just enter empty value");

        List<String> result = new ArrayList<>();
        String next = scanner.nextLine();
        while (!StringUtils.isEmpty(next)) {
            result.add(next);
            Log.info("Now value is: %s", result.toString());
            System.out.print("Enter next value: ");
            next = scanner.nextLine();
        }
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result);
    }
}
