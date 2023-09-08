package org.endava.ai.test_automation.service;

public interface TestResultHandler {

    String handleTestResult(Object context);

    String handleDescription(Object context);

    String handleFixTest(Object context);

    String logContent();


}
