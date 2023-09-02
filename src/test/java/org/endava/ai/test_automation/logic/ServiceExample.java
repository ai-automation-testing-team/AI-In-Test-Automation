package org.endava.ai.test_automation.logic;

import java.util.List;

public class ServiceExample {


    public static String sum(List<Float> integerList) {
        Float result = 0F;

        for (Float integer : integerList) {
            result += integer;
        }

        String s = String.valueOf(result);

        if (s.contains("-")) {
            throw new RuntimeException("The sum should not be negative");
        }
        return s;
    }



}
