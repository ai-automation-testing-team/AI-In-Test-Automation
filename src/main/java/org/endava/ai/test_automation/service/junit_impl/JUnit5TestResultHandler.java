package org.endava.ai.test_automation.service.junit_impl;

import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.service.TestResultHandlerImpl;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JUnit5TestResultHandler extends TestResultHandlerImpl implements AfterEachCallback {


    public static final String J_UNIT_5 = "JUnit5";


    public JUnit5TestResultHandler() {
        super(J_UNIT_5);
    }


    @Override
    public void afterEach(final ExtensionContext context) {
        String results = handleTestResult(context);
        //todo add ai results somewhere
    }


    @Override
    public String handleTestResult(Object context) {
        ExtensionContext extensionContext = (ExtensionContext) context;
        AnalysisAI analysisAI = extensionContext.getTestMethod().get().getAnnotation(AnalysisAI.class);
        if (!extensionContext.getExecutionException().isEmpty()) {
            Throwable throwable = extensionContext.getExecutionException().get();
            return handleTestFailure(analysisAI, throwable);
        }
        return null;
    }


    @Override
    public String logContent() {
        return null;
    }

}
