package org.ai.automation.test_automation.model;

import java.util.List;
import java.util.Objects;

public class MessageTemplate {

    private final String techUsed;
    private final String responseLimit;
    private final String customRequirement;
    private String logContent;


    public MessageTemplate(String techUsed, String responseLimit, String customRequirement) {
        this.techUsed = techUsed;
        this.responseLimit = responseLimit;
        this.customRequirement = customRequirement;
    }


    public void setLogContent(final String logContent) {
        this.logContent = logContent;
    }


    public String formatMessageAnalysis(String errorMessage, String stackTrace, List<String> methodCodes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
            .append("Tech used:" + this.techUsed)
            .append("\n")
            .append("Limit the response in: ").append(this.responseLimit).append(" words")
            .append("\n")
            .append("I am running test and my test is failing and this is the reason: ")
            .append("\n")
            .append(errorMessage)
            .append("\n")
            .append("Here is the stack trace:")
            .append("\n")
            .append(stackTrace)
            .append("\n")
            .append("Here is the used code:");

        methodCodes.forEach(s -> stringBuilder.append("\n").append(s));

        if (Objects.nonNull(logContent) && !logContent.isEmpty()) {
            stringBuilder
                .append("\n")
                .append("Here is the log from the execution. The error lines from the log are crucial for creating a fix")
                .append("\n")
                .append(logContent);
        }

        stringBuilder
            .append("\n")
            .append("\n")
            .append("Please try to do a good analysis and try to provide me some proposal solution for my problem.I would prefer code fixes")
            .append(this.customRequirement);
        return stringBuilder.toString();
    }


    public String formatMessageFixTest(String errorMessage, String stackTrace, List<String> methodCodes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
            .append(this.techUsed)
            .append("\n")
            .append("Limit the response in: ").append(this.responseLimit).append(" words")
            .append("\n")
            .append("I am running test and my test is failing and this is the reason: ")
            .append("\n")
            .append(errorMessage)
            .append("\n")
            .append(
                "Please try to do a good analysis and provide me single solution that contains code changes that I need to add in order for the test to work. So I would need from you to send me only code solution for fixing the test\n" +
                    "\n" +
                    "In your response please start every code block that you write with word CodeStart and end it with a word CodeEnd. Also please write always the full method code where you have some changes. Before the CodeStart line please write me the name of the class (together with package name) and the name of the method")
            .append("\n")
            .append("Here is the stack trace:")
            .append("\n")
            .append(stackTrace)
            .append("\n")
            .append("Here is the used code:");

        methodCodes.forEach(s -> stringBuilder.append("\n").append(s));

        if (Objects.nonNull(logContent) && !logContent.isEmpty()) {
            stringBuilder
                .append("\n")
                .append("Here is the log from the execution:")
                .append("\n")
                .append(logContent);
        }

        stringBuilder
            .append("\n")
            .append(this.customRequirement)
            .append("\n")
            .append("\n");
        return stringBuilder.toString();
    }


    public String formatMessageDescription(String testCode, DescUser user) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
            .append(this.techUsed)
            .append("\n")
            .append("Limit the response in: ").append(this.responseLimit).append(" words")
            .append("\n")
            .append(
                "I want you to create me a description for my automation test similar with Zephyr syntax by using this test case template.  " +
                    "Please keep the exact same format including these  \"[]\" brackets, so just replace the \"{...}\" with your content and don't send additional text beside the template populated")
            .append("\n")
            .append("\n")
            .append("Test Case Template:")
            .append("\n")
            .append("[Test Name] - {write test name here}")
            .append("\n")
            .append("[Test Summary] - {write test summary here}")
            .append("\n")
            .append("\n")
            .append(
                "[Test Step] - {write test step here} - [Test Data] - {write test data here} - [Test Result] - {write test result here}")
            .append("\n")
            .append(
                "[Test Step] - {write test step here} - [Test Data] - {write test data here} - [Test Result] - {write test result here}")
            .append("\n")
            .append(
                "[Test Step] - {write test step here} - [Test Data] - {write test data here} - [Test Result] - {write test result here}")
            .append("\n")
            .append(
                "[Test Step] - {write test step here} - [Test Data] - {write test data here} - [Test Result] - {write test result here}")
            .append("\n")
            .append(".")
            .append("\n")
            .append(".")
            .append("\n")
            .append(".")
            .append("\n")
            .append("\n").append("Also please write the description so it would be suitable for ").append(user.name()
                .toLowerCase()).append(" users. The test description should be clear and concise")
            .append("\n")
            .append("\n")
            .append("Here is test code:")
            .append("\n")
            .append(testCode);


        if (Objects.nonNull(logContent) && !logContent.isEmpty()) {
            stringBuilder
                .append("\n")
                .append("Here is the log from the execution:")
                .append("\n")
                .append(logContent);
        }

        stringBuilder
            .append("\n")
            .append("\n")
            .append(this.customRequirement);


        return stringBuilder.toString();
    }


}
