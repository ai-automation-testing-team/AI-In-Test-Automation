package org.endava.ai.test_automation.service.junit_impl;

import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.annotations.DescAI;
import org.endava.ai.test_automation.annotations.FixAI;
import org.endava.ai.test_automation.service.TestModifier;
import org.endava.ai.test_automation.service.TestResultHandlerImpl;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class JUnit5TestResultHandler extends TestResultHandlerImpl implements AfterEachCallback {


    public static final String J_UNIT_5 = "JUnit5";


    public JUnit5TestResultHandler() {
        super(J_UNIT_5);
    }


    @Override
    public void afterEach(final ExtensionContext context) {
        Class<?> aClass = context.getTestClass().get();
        String mavenBaseDirectory = getMavenBaseDirectory();
        File file = getSourceFileFromTestClass(aClass, mavenBaseDirectory);

        String analysis = handleTestResult(context);
        String description = handleDescription(context);
        TestModifier.addDescriptionAnnotation(file, context.getTestMethod().get().getName(), description);

        if (context.getExecutionException().isEmpty()) {
            TestModifier.addDescriptionAnnotation(file, context.getTestMethod().get().getName(), "Viksa");
        }
        //todo add ai results somewhere
    }


    public static String getMavenBaseDirectory() {
        try {
            String currentDir = new File(".").getCanonicalPath();
            if (new File(currentDir + "/pom.xml").exists()) {
                return currentDir;
            } else {
                throw new RuntimeException("Unable to determine Maven base directory");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error determining Maven base directory", e);
        }
    }


    public static File getSourceFileFromTestClass(Class<?> clazz, String baseDir) {
        String relativePath = "/src/test/java/";
        String packagePath = clazz.getPackage().getName().replace('.', '/');
        String fileName = clazz.getSimpleName() + ".java";
        return new File(baseDir + relativePath + packagePath + "/" + fileName);
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
