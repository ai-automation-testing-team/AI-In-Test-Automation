package org.ai.automation.test_automation.service.testNG_impl;

import org.ai.automation.test_automation.annotations.AnalysisAI;
import org.ai.automation.test_automation.annotations.DescAI;
import org.ai.automation.test_automation.annotations.FixAI;
import org.ai.automation.test_automation.service.TestResultHandlerImpl;
import org.testng.ITestResult;

import java.util.Objects;

public class TestNGTestResultHandler extends TestResultHandlerImpl {


    public static final String TEST_NG = "TestNG";


    public TestNGTestResultHandler() {
        super(TEST_NG);
    }


    @Override
    public String handleTestResult(Object context, String logContent) {
        ITestResult iTestResult = (ITestResult) context;
        AnalysisAI analysisAI = iTestResult.getMethod().getConstructorOrMethod().getMethod()
            .getAnnotation(AnalysisAI.class);
        boolean passed = iTestResult.getStatus() == ITestResult.SUCCESS;
        if (!passed) {
            Throwable throwable = iTestResult.getThrowable();
            return handleTestAnalysis(analysisAI, throwable, logContent);
        }
        return null;
    }


    @Override
    public String handleDescription(final Object context, String logContent) {
        ITestResult iTestResult = (ITestResult) context;
        DescAI descAI = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(DescAI.class);
        if (Objects.nonNull(descAI) && descAI.value()) {
            if (iTestResult.getStatus() == ITestResult.SUCCESS) {
                String s = handleTestDescription(descAI, iTestResult.getTestClass().getName(),
                    iTestResult.getMethod().getMethodName(), logContent);
                return s;
            }
        }
        return null;
    }


    @Override
    public String handleFixTest(final Object context, String logContent) {
        ITestResult iTestResult = (ITestResult) context;
        FixAI fixAI = iTestResult.getMethod().getConstructorOrMethod().getMethod()
            .getAnnotation(FixAI.class);
        if (Objects.nonNull(fixAI)) {
            boolean passed = iTestResult.getStatus() == ITestResult.SUCCESS;
            if (!passed) {
                Throwable throwable = iTestResult.getThrowable();
                return handleTestFix(fixAI, throwable, logContent);
            }
        }
        return null;
    }


}
