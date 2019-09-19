package com.blackmorse.joption.group;

import com.blackmorse.joption.SingleOption;
import com.blackmorse.joption.exceptions.CommandValidationException;
import com.blackmorse.joption.utils.ArgParserUtils;
import com.blackmorse.joption.utils.OptionsConverter;
import org.apache.commons.cli.Option;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GroupSingle implements Group {
    private final SingleOption option;

    public GroupSingle(SingleOption option) {
        this.option = option;
    }

    @Override
    public void readData(Scanner scanner, Map<String, Object> result) {
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
    }

    @Override
    public List<Option> getCliOptions() {
        return Collections.singletonList(OptionsConverter.convertToCliOption(option));
    }

    @Override
    public List<SingleOption> getSingleOptions() {
        return Collections.singletonList(option);
    }

    @Override
    public void checkGroup(Map<String, Object> valuesMap) {
        if (valuesMap.get(option.getLongName()) == null && option.isRequired()) {
            throw new CommandValidationException("Required parameter " + option.getLongName() + " is not specified");
        }
    }
}
