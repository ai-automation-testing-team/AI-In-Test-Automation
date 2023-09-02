package org.endava.ai.test_automation.model;

import java.util.List;

public class MessageTemplate {

    private final String techUsed;
    private final String responseLimit;
    private final String defaultRequirement;
    private final String customRequirement;


    public MessageTemplate(String techUsed, String responseLimit, String defaultRequirement, String customRequirement) {
        this.techUsed = techUsed;
        this.responseLimit = responseLimit;
        this.defaultRequirement = defaultRequirement;
        this.customRequirement = customRequirement;
    }


    public String formatMessage(String errorMessage, String stackTrace, List<String> methodCodes) {
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
            .append("Here is the stack trace:")
            .append("\n")
            .append(stackTrace)
            .append("\n")
            .append("Here are the methods code:");

        methodCodes.forEach(s -> stringBuilder.append("\n").append(s));

        stringBuilder
            .append("\n")
            .append("\n")
            .append(this.defaultRequirement)
            .append(this.customRequirement);

        return stringBuilder.toString();
    }

}
