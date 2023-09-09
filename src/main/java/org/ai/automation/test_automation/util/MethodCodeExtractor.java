package org.ai.automation.test_automation.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.aeonbits.owner.ConfigCache;
import org.ai.automation.test_automation.annotations.AnalysisAI;
import org.ai.automation.test_automation.config.AIConfig;
import org.ai.automation.test_automation.model.SendContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MethodCodeExtractor {

    private static final String ROOT_DIRECTORY = "src";
    protected static final AIConfig aiConfig = ConfigCache.getOrCreate(AIConfig.class);


    public static List<String> extractMethodCodes(Exception exception) {
        StackTraceElement[] stackTraceElements = exception.getStackTrace();

        List<String> methodCodes = new ArrayList<>();

        boolean found = false;
        for (StackTraceElement element : stackTraceElements) {
            if (found) {
                break;
            }
            try {
                Class<?> clazz = Class.forName(element.getClassName());

                if (Objects.nonNull(aiConfig.forbiddenPackages()) && !aiConfig.forbiddenPackages()
                    .isEmpty() && Arrays.asList(aiConfig.forbiddenPackages().split(";"))
                    .contains(element.getClassName().substring(0, element.getClassName().lastIndexOf(".")))) {
                    continue;
                }

                if (SendContext.CLASS.equals(SendContext.valueOf(aiConfig.sendContext()))) {
                    String code = getFullClassCode(clazz.getName());

                    if (code != null) {
                        methodCodes.add(code);

                    }

                    if (hasMethodWithAnnotation(clazz, AnalysisAI.class)) {
                        found = true;
                    }
                    continue;
                }

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(element.getMethodName())) {
                        String code = getFullMethodCode(clazz.getName(), method.getName());


                        if (code != null) {
                            methodCodes.add(code);
                        }

                        if (method.isAnnotationPresent(AnalysisAI.class)) {
                            found = true;
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        Collections.reverse(methodCodes);
        return methodCodes;
    }


    public static String getFullMethodCode(String className, String methodName) throws IOException {
        Path rootPath = Paths.get(ROOT_DIRECTORY);
        Path file = findFile(className, rootPath);

        try (FileInputStream in = new FileInputStream(file.toFile())) {
            JavaParser parser = new JavaParser();
            CompilationUnit cu = parser.parse(in)
                .getResult()
                .orElseThrow(() -> new RuntimeException("Unable to parse the file: " + file));

            return findMethodInCompilationUnit(cu, methodName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + file, e);
        } catch (ParseProblemException e) {
            throw new RuntimeException("Problem parsing the file: " + file, e);
        }
    }


    private static boolean hasMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }


    private static String getFullClassCode(String className) throws IOException {
        Path rootPath = Paths.get(ROOT_DIRECTORY);
        Path file = findFile(className, rootPath);

        try (FileInputStream in = new FileInputStream(file.toFile())) {
            JavaParser parser = new JavaParser();
            CompilationUnit cu = parser.parse(in)
                .getResult()
                .orElseThrow(() -> new RuntimeException("Unable to parse the file: " + file));

            return findClassInCompilationUnit(cu, className);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + file, e);
        } catch (ParseProblemException e) {
            throw new RuntimeException("Problem parsing the file: " + file, e);
        }
    }


    private static String findClassInCompilationUnit(CompilationUnit cu, String className) {
        String simpleClassName = className.contains(".") ?
            className.substring(className.lastIndexOf('.') + 1) :
            className;
        Optional<ClassOrInterfaceDeclaration> classDec = cu.getClassByName(simpleClassName);
        if (classDec.isPresent()) {
            return classDec.get().toString();
        } else {
            throw new RuntimeException("Class not found: " + className);
        }
    }


    private static String findMethodInCompilationUnit(CompilationUnit cu, String methodName) {
        return cu.findAll(MethodDeclaration.class).stream()
            .filter(m -> m.getName().asString().equals(methodName))
            .findFirst()
            .map(MethodDeclaration::toString)
            .orElseThrow(() -> new RuntimeException("Missing method with name: " + methodName));
    }


    public static Path findFile(String className, Path searchDirectory) throws IOException {
        String fileName = className.replace('.', '/') + ".java";

        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher("glob:**/" + fileName);

        try (Stream<Path> paths = Files.walk(searchDirectory)) {
            return paths
                .filter(path -> Files.isRegularFile(path) && matcher.matches(path))
                .findFirst()
                .orElse(null);
        }
    }


}
