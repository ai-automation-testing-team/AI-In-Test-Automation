package org.endava.ai.test_automation.tests;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.endava.ai.test_automation.service.HtmlBuilder.buildHtmlFromContent;

public class HtmlTest {


    @Test
    public void deleteMeee() throws IOException {
        String content = "Based on the provided stack trace, the test fails because the sum of the elements in the list is negative, which violates the requirement of the `sum` method. The error is thrown when the sum of the list elements is negative.\n" +
            "\n" +
            " \n" +
            "\n" +
            "One possible solution to fix the issue is to modify the `sum` method to check for negative values before the sum calculation and throw an exception immediately if a negative value is found. Here's an updated version of the method:\n" +
            "\n" +
            " \n" +
            "\n" +
            "CodeStart\n" +
            "public static String sum(List<Float> integerList) {\n" +
            "    for (Float integer : integerList) {\n" +
            "        if (integer < 0) {\n" +
            "            throw new RuntimeException(\"The sum should not be negative\");\n" +
            "        }\n" +
            "    }\n" +
            "    Float result = 0F;\n" +
            "    for (Float integer : integerList) {\n" +
            "        result += integer;\n" +
            "    }\n" +
            "    String s = String.valueOf(result);\n" +
            "    return s;\n" +
            "}\n" +
            "CodeEnd\n" +
            "\n" +
            " \n" +
            "\n" +
            "With this modification, the method checks for negative values before performing the sum calculation. If a negative value is found, it immediately throws a `RuntimeException` with the appropriate error message.\n" +
            "\n" +
            " \n" +
            "\n" +
            "By updating the `sum` method as shown above, the test should pass successfully if all the elements in the list are positive or zero. If the test fails with negative elements, it will throw the expected exception with the \"The sum should not be negative\" message.";
        String extected; //ide vo anotacija
        String html = buildHtmlFromContent(content);
        //ide vo allure
        System.out.println(html);
    }

}
