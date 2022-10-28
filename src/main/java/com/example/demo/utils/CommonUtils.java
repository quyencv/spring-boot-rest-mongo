package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class CommonUtils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static String obtainParameter(HttpServletRequest request, String parameter) {
        String obtain = request.getParameter(parameter);
        return StringUtils.isNotEmpty(obtain) ? obtain : StringUtils.EMPTY;
    }
}
