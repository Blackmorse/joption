package com.blackmorse.joption.group;

import com.blackmorse.joption.SingleOption;
import org.apache.commons.cli.Option;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface Group {
    void readData(Scanner scanner, Map<String, Object> result);

    List<Option> getCliOptions();
    List<SingleOption> getSingleOptions();
    void checkGroup(Map<String, Object> valuesMap);
}
