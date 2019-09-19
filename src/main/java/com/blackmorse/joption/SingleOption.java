package com.blackmorse.joption;

public class SingleOption {
    private final String shortName;
    private final String longName;
    private final ARG_NUM argsNum;
    private final boolean required;
    private final String description;
    private final Object defaultValue;

    private SingleOption(String shortName, String longName, ARG_NUM argsNum, boolean required, String description, Object defaultValue) {
        this.shortName = shortName;
        this.longName = longName;
        this.argsNum = argsNum;
        this.required = required;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public ARG_NUM getArgsNum() {
        return argsNum;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public enum ARG_NUM {
        NO_ARG,
        ONE_ARG,
        MULTIPLY_ARGS
    }

    public static SingleOptionBuilder builder() {
        return new SingleOptionBuilder();
    }

    public static final class SingleOptionBuilder {
        private String shortName;
        private String longName;
        //TODO Custom builder accept as default type parameter depends on ARG_NUM value (String, List<String>, boolean)
        private ARG_NUM argsNum;
        private boolean required;
        private String description;
        private Object defaultValue;

        private SingleOptionBuilder() {
        }

        public SingleOptionBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public SingleOptionBuilder longName(String longName) {
            this.longName = longName;
            return this;
        }

        public SingleOptionBuilder argsNum(ARG_NUM argsNum) {
            this.argsNum = argsNum;
            return this;
        }

        public SingleOptionBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        public SingleOptionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public SingleOptionBuilder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public SingleOption build() {
            if (shortName == null) {
                shortName = longName;
            }
            if (longName == null) {
                longName = shortName;
            }
            return new SingleOption(shortName, longName, argsNum, required, description, defaultValue);
        }
    }
}
