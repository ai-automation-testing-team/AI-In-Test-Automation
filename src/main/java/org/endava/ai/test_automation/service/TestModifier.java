package org.endava.ai.test_automation.service;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestModifier {


    public static void addDescriptionAnnotation(File sourceFile, String methodName, String descriptionValue) {
        try {
            // Parse the Java file
            JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, sourceFile);

            // Find the method by name
            MethodSource<?> method = javaClass.getMethod(methodName);

            if (method != null) {
                // Add or update the @Description annotation
                AnnotationSource<?> descriptionAnnotation = method.getAnnotation("Description");
                if (descriptionAnnotation == null) {
                    descriptionAnnotation = method.addAnnotation("Description");
                }
                descriptionAnnotation.setStringValue(descriptionValue);

                // Write the modified source back to the file
                try (FileWriter writer = new FileWriter(sourceFile)) {
                    writer.write(javaClass.toString());
                }
            } else {
                System.out.println("Method not found: " + methodName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
