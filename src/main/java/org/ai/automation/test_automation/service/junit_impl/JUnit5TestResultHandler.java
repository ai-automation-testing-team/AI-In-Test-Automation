package org.ai.automation.test_automation.service.junit_impl;

import org.ai.automation.test_automation.annotations.AnalysisAI;
import org.ai.automation.test_automation.annotations.DescAI;
import org.ai.automation.test_automation.annotations.FixAI;
import org.ai.automation.test_automation.service.TestResultHandlerImpl;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Objects;

public class JUnit5TestResultHandler extends TestResultHandlerImpl {


    public static final String J_UNIT_5 = "JUnit5";


    public JUnit5TestResultHandler() {
        super(J_UNIT_5);
    }


    @Override
    public String handleTestResult(Object context) {
        ExtensionContext extensionContext = (ExtensionContext) context;
        AnalysisAI analysisAI = extensionContext.getTestMethod().get().getAnnotation(AnalysisAI.class);
        if (!extensionContext.getExecutionException().isEmpty()) {
            Throwable throwable = extensionContext.getExecutionException().get();
            String s = handleTestAnalysis(analysisAI, throwable);
            return s;
        }
        return null;
    }


    @Override
    public String handleDescription(final Object context) {
        ExtensionContext extensionContext = (ExtensionContext) context;
        DescAI descAI = extensionContext.getTestMethod().get().getAnnotation(DescAI.class);
        if (Objects.nonNull(descAI) && descAI.value()) {
            if (extensionContext.getExecutionException().isEmpty()) {
                String s = handleTestDescription(descAI,
                    ((ExtensionContext) context).getTestClass().get().getSimpleName(),
                    ((ExtensionContext) context).getTestMethod().get().getName());
                return s;
            }
        }
        return null;
    }


    @Override
    public String handleFixTest(final Object context) {
        ExtensionContext extensionContext = (ExtensionContext) context;
        FixAI fixAI = extensionContext.getTestMethod().get().getAnnotation(FixAI.class);
        if (Objects.nonNull(fixAI)) {
            if (!extensionContext.getExecutionException().isEmpty()) {
                Throwable throwable = extensionContext.getExecutionException().get();
                String s = handleTestFix(fixAI, throwable);
                return s;
            }
        }
        return null;
    }


    @Override
    public String logContent() {
        return null;
    }

}
