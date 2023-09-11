package org.ai.automation.test_automation.service;

public interface TestResultHandler {

    String handleTestResult(Object context, String logContent);

    String handleDescription(Object context, String logContent);

    String handleFixTest(Object context, String logContent);


}
