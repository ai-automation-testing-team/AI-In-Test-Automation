package org.ai.automation.test_automation.service;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.ValuePair;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestModifier {


    public static void addAnnotation(File sourceFile, String methodName, String descriptionValue) {
        try {
            JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, sourceFile);

            MethodSource<?> method = javaClass.getMethod(methodName);


            if (method != null) {
                List<ValuePair> values = method.getAnnotation("org.ai.automation.test_automation.annotations.DescAI")
                    .getValues();

                boolean enableDesc = values.isEmpty() || Boolean.parseBoolean(
                    values.get(0)
                        .getStringValue());

                if (enableDesc) {
                    AnnotationSource<?> descAI = method.getAnnotation(
                        "org.ai.automation.test_automation.annotations.DescAI");
                    if (descAI == null) {
                        descAI = method.addAnnotation("org.ai.automation.test_automation.annotations.DescAI");
                    }
                    descAI.setLiteralValue("value", "false");
                    descAI.setStringValue("content", descriptionValue);

                    // Write the modified source back to the file
                    try (FileWriter writer = new FileWriter(sourceFile)) {
                        writer.write(javaClass.toString());
                    }
                }
            } else {
                System.out.println("Method not found: " + methodName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void replaceMethod(File sourceFile, String fullClassName, String methodName, String newMethodBody) {
        try {
            JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, sourceFile);

            // Check if the current class is the target class
            if (javaClass.getQualifiedName().equals(fullClassName)) {
                // Find the method we want to replace
                MethodSource<?> method = javaClass.getMethod(methodName, String.class);

                // Remove the old method
                if (method != null) {
                    javaClass.removeMethod((Method<JavaClassSource, ?>) method);
                } else {
                    System.out.println("Method not found: " + methodName);
                    return;
                }

                // Add the new method
                javaClass.addMethod(newMethodBody);

                // Write the modified source back to the file
                try (FileWriter writer = new FileWriter(sourceFile)) {
                    writer.write(javaClass.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
