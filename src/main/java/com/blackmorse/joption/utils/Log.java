package com.blackmorse.joption.utils;

public class Log {
    private static final String RESET = "\033[0m";
    private static final String YELLOW = "\033[0;33m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String YELLOW_BOLD = "\033[1;33m";

    public static synchronized void log(String str, String color) {
        System.out.println(color + str + RESET);
    }

    public static synchronized void log(String str) {
        log(str,RESET);
    }

    public static synchronized void err(String str) {
        System.out.println(RED_BOLD + str + RESET);
    }

    public static synchronized void err(String str, String val) {
        System.out.println(String.format(str, RED_BOLD + val + RESET));
    }

    public static synchronized void info(String str) {
        System.out.println(YELLOW + str + RESET);
    }

    public static synchronized void info(String str, String val) {
        System.out.println(String.format(str, YELLOW + val + RESET));
    }

    public static synchronized void important(String str) {
        System.out.println(YELLOW_BOLD + str + RESET);
    }

    public static synchronized void important(String str, String val) {
        System.out.println(String.format(str, YELLOW_BOLD + val + RESET));
    }
}
