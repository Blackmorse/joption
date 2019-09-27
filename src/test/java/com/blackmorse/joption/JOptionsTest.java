package com.blackmorse.joption;

import com.blackmorse.joption.group.GroupSingle;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JOptionsTest {

    @Test(expected = ClassCastException.class)
    public void testGetMultiplyArgForOneArg() throws ParseException {
        JOptions jOptions = new JOptions();

        SingleOption option = SingleOption.builder()
                .shortName("name")
                .argsNum(SingleOption.ARG_NUM.ONE_ARG)
                .required(true)
                .build();

        GroupSingle group = new GroupSingle(option);

        jOptions.addGroups(group);

        jOptions.parse(new String[]{"--name", "value"});

        assertEquals("value", jOptions.getOneArgValue("name"));
        jOptions.getMultiplyArgValues("name");
    }
}