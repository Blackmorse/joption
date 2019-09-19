package com.blackmorse.joption.utils;

import com.blackmorse.joption.SingleOption;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;

public class OptionsConverter {
    public static Option convertToCliOption(SingleOption singleOption) {
        Option.Builder builder = Option.builder(singleOption.getShortName());
        if (!StringUtils.isEmpty(singleOption.getLongName())) {
            builder.longOpt(singleOption.getLongName());
        }
        builder.required(singleOption.isRequired());
        builder.desc(singleOption.getDescription());
        switch (singleOption.getArgsNum()) {
            case NO_ARG: builder.hasArg(false);
                break;
            case ONE_ARG: builder.hasArg();
                break;
            case MULTIPLY_ARGS: builder.numberOfArgs(Option.UNLIMITED_VALUES);
                break;
        }

        return builder.build();
    }
}