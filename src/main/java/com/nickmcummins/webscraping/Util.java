package com.nickmcummins.webscraping;

public class Util {
    public static void print(String message, Object... args) {
        System.out.println(String.format(message, args));
    }

    public static void print(Object object) {
        System.out.println(object);
    }
}
