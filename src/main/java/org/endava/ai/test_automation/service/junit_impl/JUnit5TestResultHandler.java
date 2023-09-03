package org.endava.ai.test_automation.service.junit_impl;

import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.service.TestModifier;
import org.endava.ai.test_automation.service.TestResultHandlerImpl;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.io.IOException;

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

        TestModifier.addDescriptionAnnotation(file, context.getTestMethod().get().getName(), "Viksa");

        // String results = handleTestResult(context);

        //todo add ai results somewhere
    }

    public static String getMavenBaseDirectory() {
        try {
            // Path to current working directory
            String currentDir = new File(".").getCanonicalPath();

            // Check if pom.xml exists in the current directory
            if (new File(currentDir + "/pom.xml").exists()) {
                return currentDir;
            } else {
                // You can search parent directories or other locations here if needed
                throw new RuntimeException("Unable to determine Maven base directory");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error determining Maven base directory", e);
        }
    }

    private static File getSourceFileFromTestClass(Class<?> clazz, String baseDir) {
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
            String s = handleTestFailure(analysisAI, throwable);
            return s;
        }
        return null;
    }


    @Override
    public String logContent() {
        return null;
    }

}
