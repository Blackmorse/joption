package com.blackmorse.joption;

import com.blackmorse.joption.exceptions.CommandValidationException;
import com.blackmorse.joption.group.Group;
import com.blackmorse.joption.utils.OptionsConverter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class JOptions {
    private final org.apache.commons.cli.CommandLineParser parser = new DefaultParser();
    private final List<Group> groups = new ArrayList<>();
    private Map<String, Object> resultMap;

    public void addGroups(Group... group) {
        groups.addAll(Arrays.asList(group));
    }

    public boolean getNoArgValue(String name) {
        return (boolean) resultMap.get(name);
    }

    public String getOneArgValue(String name) {
        return (String) resultMap.get(name);
    }

    public List<String> getMultiplyArgValues(String name) {
        return (List<String>) resultMap.get(name);
    }

    public void parse(Map<String, Object> parametersMap) {
        Map<String, Object> resultMap = new HashMap<>(parametersMap);
        groups.forEach(group -> group.getSingleOptions().forEach(singleOption -> {
            if (parametersMap.get(singleOption.getLongName()) == null && singleOption.getDefaultValue() != null) {
                resultMap.put(singleOption.getLongName(), singleOption.getDefaultValue());
            }
        }));
        groups.forEach(group -> group.checkGroup(resultMap));
        this.resultMap = resultMap;
    }

    public void printHelp() {
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

        groups.forEach(group -> group.getSingleOptions().forEach(option -> options.addOption(OptionsConverter.convertToCliOption(option))));
        new HelpFormatter().printHelp("ant", options);
    }

    public void parse() {
        Map<String, Object> result = new HashMap<>();
        try (Scanner scanner = new Scanner(System.in)) {
            for (Group group : groups) {
                group.readData(scanner, result);
            }
        }
        this.resultMap = result;
    }

    public void parse(String[] args) throws ParseException {
        org.apache.commons.cli.Options cliOptions = new org.apache.commons.cli.Options();
        List<SingleOption> singleOptions = new ArrayList<>();
        groups.forEach(group -> group.getCliOptions().forEach(cliOptions::addOption));
        groups.forEach(group -> singleOptions.addAll(group.getSingleOptions()));

        CommandLine cmd = parser.parse(cliOptions, args);
        Map<String, Object> result = convertToMap(singleOptions, Arrays.asList(cmd.getOptions()));

        groups.forEach(group -> group.checkGroup(result));
        this.resultMap =  result;
    }

    private Map<String, Object> convertToMap(List<SingleOption> commandLineOptions, List<Option> cmdOptions) {
        Map<String, Object> valuesMap = new HashMap<>();

        for (SingleOption programOption : commandLineOptions) {
            Optional<Option> cmdOpt = cmdOptions.stream()
                    .filter(cmdOption -> cmdOption.getLongOpt().equals(programOption.getLongName()))
                    .findAny();

            if (!cmdOpt.isPresent()) {
                if (programOption.getArgsNum().equals(SingleOption.ARG_NUM.NO_ARG)) {
                    valuesMap.put(programOption.getLongName(), false);
                } else if (programOption.isRequired()) {
                    throw new CommandValidationException(programOption.getLongName() + " is not specified");
                } else {
                    valuesMap.put(programOption.getLongName(), programOption.getDefaultValue());
                }
            } else {
                Option cmdOption = cmdOpt.get();
                switch (programOption.getArgsNum()) {
                    case ONE_ARG:
                        if (programOption.isRequired()) {
                            if (cmdOption.getValue() == null) {
                                throw new CommandValidationException(programOption.getLongName() + " is not specified");
                            }
                            valuesMap.put(programOption.getLongName(), cmdOption.getValue());
                        } else {
                            if (cmdOption.getValue() == null) {
                                valuesMap.put(programOption.getLongName(), programOption.getDefaultValue());
                            } else {
                                valuesMap.put(programOption.getLongName(), cmdOption.getValue());
                            }
                        }
                        break;
                    case MULTIPLY_ARGS:
                        if (programOption.isRequired()) {
                            if (cmdOption.getValue() == null) {
                                throw new CommandValidationException(programOption.getLongName() + " is not specified");
                            }
                            valuesMap.put(programOption.getLongName(), Arrays.asList(cmdOption.getValues()));
                        } else {
                            if (cmdOption.getValue() == null) {
                                valuesMap.put(programOption.getLongName(), programOption.getDefaultValue());
                            } else {
                                valuesMap.put(programOption.getLongName(), Arrays.asList(cmdOption.getValues()));
                            }
                        }
                        break;
                    case NO_ARG:
                        valuesMap.put(programOption.getLongName(), true);
                        break;
                }
            }
        }
        return valuesMap;
    }
}
