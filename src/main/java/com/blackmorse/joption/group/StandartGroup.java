package com.blackmorse.joption.group;

import com.blackmorse.joption.SingleOption;
import com.blackmorse.joption.exceptions.CommandValidationException;
import com.blackmorse.joption.utils.ArgParserUtils;
import com.blackmorse.joption.utils.Log;
import com.blackmorse.joption.utils.OptionsConverter;
import org.apache.commons.cli.Option;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StandartGroup implements Group {
    private final List<SingleOption> options;

    public StandartGroup(List<SingleOption> options) {
        this.options = options;
    }

    public void readData(Scanner scanner, Map<String, Object> result) {
        for (SingleOption option : options) {
            switch (option.getArgsNum()) {
                case NO_ARG:
                    result.put(option.getLongName(), ArgParserUtils.parseStandardNoArgs(option, scanner));
                    break;
                case ONE_ARG:
                    ArgParserUtils.parseStandartOneArg(option, scanner).ifPresent(s -> result.put(option.getLongName(), s));
                    break;
                case MULTIPLY_ARGS:
                    ArgParserUtils.parseStandardMultiplyArgs(option, scanner).ifPresent(l -> result.put(option.getLongName(), l));
                    break;
            }
            Log.log("_______________________________");
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
        for (SingleOption option : options) {
            if (valuesMap.get(option.getLongName()) == null && option.isRequired()) {
                throw new CommandValidationException("Required parameter " + option.getLongName() + " is not specified");
            }
        }
    }
}
