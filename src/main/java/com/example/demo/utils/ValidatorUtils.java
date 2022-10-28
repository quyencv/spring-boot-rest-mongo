package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {

    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static boolean isValid(final String str, final Pattern pattern) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
