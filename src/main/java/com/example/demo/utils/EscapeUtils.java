package com.example.demo.utils;

public class EscapeUtils {

    public static String escapeStringForMySQL(String txt) {
        return txt.replaceAll("\b", "\\b").replaceAll("\n", "\\n")
                .replaceAll("\r", "\\r").replaceAll("\t", "\\t").replaceAll("\\x1A", "\\Z").replaceAll("\\x00", "\\0")
                .replaceAll("'", "\\'").replaceAll("\"", "\\\"");
    }

    public static String escapeWildcardsForMySQL(String txt) {
        return escapeStringForMySQL(txt).replaceAll("%", "\\%").replaceAll("_", "\\_");
    }
}