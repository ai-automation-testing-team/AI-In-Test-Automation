package org.endava.ai.test_automation.service.testNG_impl;

import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.annotations.DescAI;
import org.endava.ai.test_automation.annotations.FixAI;
import org.endava.ai.test_automation.service.TestResultHandlerImpl;
import org.testng.ITestResult;

import java.util.Objects;

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
            return handleTestAnalysis(analysisAI, throwable);
        }
        return null;
    }


    @Override
    public String handleDescription(final Object context) {
        ITestResult iTestResult = (ITestResult) context;
        DescAI descAI = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(DescAI.class);
        if (Objects.nonNull(descAI) && descAI.value()) {
            if (iTestResult.getStatus() == ITestResult.SUCCESS) {
                String s = handleTestDescription(descAI, iTestResult.getTestClass().getName(),
                    iTestResult.getMethod().getMethodName());
                return s;
            }
        }
        return null;
    }


    @Override
    public String handleFixTest(final Object context) {
        ITestResult iTestResult = (ITestResult) context;
        FixAI fixAI = iTestResult.getMethod().getConstructorOrMethod().getMethod()
            .getAnnotation(FixAI.class);
        if (Objects.nonNull(fixAI)) {
            boolean passed = iTestResult.getStatus() == ITestResult.SUCCESS;
            if (!passed) {
                Throwable throwable = iTestResult.getThrowable();
                return handleTestFix(fixAI, throwable);
            }
        }
        return null;
    }


    @Override
    public String logContent() {
        return null;
    }

}
