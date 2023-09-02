package org.endava.ai.test_automation.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.endava.ai.test_automation.annotations.AnalysisAI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodCodeExtractor {

    private static final String ROOT_DIRECTORY = "src";


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
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(element.getMethodName())) {
                        String fullMethodCode = getFullMethodCode(clazz.getName(), method.getName());
                        if (fullMethodCode != null) {
                            methodCodes.add(fullMethodCode);
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


    private static String getFullMethodCode(String className, String methodName) throws IOException {
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
