package org.ai.automation.test_automation.config;


import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;

public class AITokenConverter implements Converter<String> {


    @Override
    public String convert(final Method method, final String s) {
        if (s == null || s.isEmpty()) {
            String property = System.getProperty("ai.token");
            return property;
        }
        return s;
    }

}
