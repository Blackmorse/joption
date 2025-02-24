# joption _(alpha version)_
Library for passing params to Java apps. Based on _`commons cli`_ (https://github.com/apache/commons-cli)

Allow to pass parameters to java app through 
 - command line arguments,
 - programmatically using `Map<String, Object>`,
 - console interactive mode
 
# Description
  
Organize options in groups.
Group categories: 
 - `GroupSingle`. Contains just one _SingleOption_)
 - `SelectOneGroup`. Contains any number of _SingleOption_, user allowed to choose only one of them. _SingleOption.required_ doesn't impact on Options in this group
 - `StandartGroup`. Contains any number of _SingleOption_, _SingleOption.required_ responsive for mandatory options.
 
## Configuring
 
#### Creating `SingleOption`:
 
 ```
 import com.blackmorse.joption.SingleOption
 SingleOption singleOption = SingleOption.builder()
        .shortName("g")
         .longName("granularity")
         .required(false)
         .argsNum(SingleOption.ARG_NUM.ONE_ARG)
         .defaultValue("1")
         .description("For cases when you need to decrease sample or modulo for aggregating script. By default we use 1/shards_count ")
     .build();
 ```
 There are 3 types of argsNum: _ONE_ARG_, _NO_ARG_, _MULTIPLY_ARGS_
#### Creating _groups_
 
```
import com.blackmorse.joption.group.*;

StandartGroup standartGroup = new StandartGroup(Arrays.asList(<options>));
SelectOneGroup selectOneGroup = new SelectOneGroup("name", Arrays.asList(<options>))
GroupSingle groupSingle = new GroupSingle(option);
```

#### Creating `JOptions`
```
import com.blackmorse.joption.JOptions;
JOptions joptions = new JOptions();
joptions.addGroups(standartGroup, selectOneGroup, groupSingle);
```

## Using
#### Passing arguments with interactive mode
```
options.parse();
```
Interactive mode will be started, allowing your to pass arguments from console
#### Passing command line arguments
```
public static void main(String[] args) {
    joptions.parse(args);
}
```
jOption will parse argument just like _`commons cli`_

#### Using Map<String, Object>
```
Map<String, Object> input = ...;
joptions.parse(input);
```
Library will check consistency of _input_, check groups, required arguments, pass defaults and return _parameters_.

## Get parsed arguments
```
boolean b = joptions.getNoArgValue(..);
String value = joptions.getOneArgValue(..);
List<String> list = joptions.getMultiplyArgValues(..);
``` 
Use this methods(method correponds to `SingleOption.ARG_NUM` type of provided `SingleOption`) for fetching parsed arguments