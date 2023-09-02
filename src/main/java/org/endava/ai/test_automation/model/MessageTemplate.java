package org.endava.ai.test_automation.model;

import java.util.List;
import java.util.Objects;

public class MessageTemplate {

    private final String techUsed;
    private final String responseLimit;
    private final String defaultRequirement;
    private final String customRequirement;
    private final String logContent;


    public MessageTemplate(String techUsed, String responseLimit, String defaultRequirement, String customRequirement, String logContent) {
        this.techUsed = techUsed;
        this.responseLimit = responseLimit;
        this.defaultRequirement = defaultRequirement;
        this.customRequirement = customRequirement;
        this.logContent = logContent;
    }


    public String formatMessage(String errorMessage, String stackTrace, List<String> methodCodes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
            .append(this.techUsed)
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

        if(Objects.nonNull(logContent) && !logContent.isEmpty()) {
            stringBuilder
                .append("\n")
                .append("Here is the log from the execution:")
                .append("\n")
                .append(logContent);
        }

        stringBuilder
            .append("\n")
            .append("\n")
            .append(this.defaultRequirement)
            .append(this.customRequirement);

        return stringBuilder.toString();
    }

}
