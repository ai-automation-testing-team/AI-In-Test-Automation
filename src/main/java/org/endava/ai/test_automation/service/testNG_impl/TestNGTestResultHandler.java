package org.endava.ai.test_automation.service.testNG_impl;

import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.service.TestResultHandlerImpl;
import org.testng.ITestResult;
import org.testng.annotations.Test;

public class TestNGTestResultHandler extends TestResultHandlerImpl {


    public static final String TEST_NG = "TestNG";


    public TestNGTestResultHandler() {
        super(TEST_NG);
    }


    @Override
    public String handleTestResult(Object context) {
        ITestResult iTestResult = (ITestResult) context;
        AnalysisAI analysisAI = iTestResult.getMethod().getConstructorOrMethod().getMethod()
            .getAnnotation(AnalysisAI.class);
        boolean passed = iTestResult.getStatus() == ITestResult.SUCCESS;
        if (!passed) {
            Throwable throwable = iTestResult.getThrowable();
            return handleTestFailure(analysisAI, throwable);
        }
        return null;
    }

}
