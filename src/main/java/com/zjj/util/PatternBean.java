package com.zjj.util;

import java.util.regex.Pattern;

public class PatternBean {

    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

    public static Pattern getNumberPattern() {
        // Avoid use Pattern.compile in method body.
        Pattern localPattern = PatternBean.NUMBER_PATTERN;
        return localPattern;
    }
}
