package ua.scudy.server.compiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.data.assignment.AssignmentTestCaseData;
import ua.scudy.server.data.assignment.solution.*;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
@Slf4j
public class SolutionCodeCompiler {

    @Value("${scudy.file.assignments.solutions.path}")
    private String solutionsPath;
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    public SolutionType compileAssignmentSolution(String code, File solutionFile, AssignmentData assignmentData)
            throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var root = new File(solutionsPath.split("/")[0]);

        var solutionFilePath = solutionFile.getPath().split("/");
        var solutionFilePackage = solutionFilePath[solutionFilePath.length-2];

        var solutionFile2 = new File(root, String.format("%s/Task.java", solutionFilePackage));

        compiler.run(null, null, null, solutionFile2.getPath());
        List<String> syntaxErrors = checkClass(solutionFile2.getPath());
        if(syntaxErrors.isEmpty()) {
            if(checkStartCodeChanges(assignmentData.getAssignmentCondition().getAssignmentConditionCode(), code)) {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                Class<?> cls = Class.forName(String.format("%s.Task", solutionFilePackage), false, classLoader);
                Object instance = cls.getDeclaredConstructor().newInstance();

                Method solutionMethod = cls.getDeclaredMethods()[0];
                log.info("method = {}", solutionMethod.getName());

                List<AssignmentTestCaseData> testCases = assignmentData.getAssignmentTestCaseData();
                int testCasePassed = 1;
                try {
                    for (var testCase : testCases) {
                        log.info("test case = {}", testCase);
                        Class<?> argType = Class.forName(String.format("java.lang.%s", testCase.getArgumentType()));
                        log.info("size = {}", testCase.getArgumentArraySize() > 0);
                        if (testCase.getArgumentArraySize() > 0) {
                            log.info("return = {}", convertArrayArg(argType, testCase.getArgumentArrayValues()));
                            Object argsArray = convertArrayArg(argType, testCase.getArgumentArrayValues());
//                        String result = (String) cls.getDeclaredMethod(assignmentData.getAssignmentCondition().getMethodName(),
//                                argsArray.getClass()).invoke(instance, argsArray);
                            String result = (String) solutionMethod.invoke(instance, argsArray);
                            if (result.equals(testCase.getOutputString())) {
                                testCasePassed++;
                            } else {
                                log.warn("result = {}, output = {}", result, testCase.getOutputString());
                                log.warn("test case result = {}, test case waiting result = {}, isEquals = {}",
                                        result, testCase.getOutputString(), Objects.equals(result, testCase.getOutputString()));
                                throw new Exception("Test case " + testCasePassed + " not passed");
                            }
                        } else {
                            Object arg = convertArg(argType, testCase.getArgumentValue());
//                        String result = (String) cls.getDeclaredMethod(assignmentData.getAssignmentCondition().getMethodName(),
//                                arg.getClass()).invoke(instance, arg);
                            String result = (String) solutionMethod.invoke(instance, arg);
                            log.info("result = {}, output = {}", result, testCase.getOutputString());
                            log.info("test case result = {}, test case waiting result = {}, isEquals = {}",
                                    result, testCase.getOutputString(), Objects.equals(result, testCase.getOutputString()));
                            if (result.equals(testCase.getOutputString())) {
                                testCasePassed++;
                            } else {
                                throw new Exception("Test case " + testCasePassed + " not passed");
                            }
                        }
                    }
                } catch (Exception ex) {
                    // TODO: returned arg value for array and single argument
                    return new FailedSolution(assignmentData.getAssignmentTestCaseData().size(),
                            testCasePassed, testCases.get(testCasePassed - 1).getArgumentType(),
                            testCases.get(testCasePassed - 1).getArgumentArraySize() > 0,
                            Arrays.toString(testCases.get(testCasePassed - 1).getArgumentArrayValues()));
                }
                return new DoneSolution("Solution accepted");

            } else {
                return new FailedSolutionMessage("The original appearance of the code is broken.");
            }
        }
        return new FailedSyntaxSolution(syntaxErrors);
    }

    public Object convertArg(Class<?> argClass, Object argValue) {
        var value = (String) argValue;
        if(argClass == Boolean.class) { return Boolean.parseBoolean(value); }
        if(argClass == String.class) { return String.valueOf(value); }
        if(argClass == Byte.class) { return Byte.parseByte(value); }
        if(argClass == Short.class) { return Short.parseShort(value); }
        if(argClass == Integer.class) { return Integer.parseInt(value); }
        if(argClass == Long.class) { return Long.parseLong(value); }
        if(argClass == Float.class) { return Float.parseFloat(value); }
        if(argClass == Double.class) { return Double.parseDouble(value); }
        return value;
    }

    public Object convertArrayArg(Class<?> argClass, Object[] argValues) {
        Object[] arrayToReturn = null;
        if(argClass == Boolean.class) { arrayToReturn = new Boolean[argValues.length]; }
        if(argClass == String.class) { arrayToReturn = new String[argValues.length]; }
        if(argClass == Byte.class) { arrayToReturn = new Byte[argValues.length]; }
        if(argClass == Short.class) { arrayToReturn = new Short[argValues.length]; }
        if(argClass == Integer.class) { arrayToReturn = new Integer[argValues.length]; }
        if(argClass == Long.class) { arrayToReturn = new Long[argValues.length]; }
        if(argClass == Float.class) { arrayToReturn = new Float[argValues.length]; }
        if(argClass == Double.class) { arrayToReturn = new Double[argValues.length]; }
        if (argValues.length - 1 >= 0) {
            assert arrayToReturn != null;
            System.arraycopy(argValues, 0, arrayToReturn, 0, argValues.length);
            log.info("array = {}", arrayToReturn);
        }
        return arrayToReturn;
    }

    public static List<String> checkClass(String file) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromStrings(Arrays.asList(file));

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();

        List<String> messages = new ArrayList<>();
        for (var diagnostic : diagnostics.getDiagnostics()) {
            messages.add(diagnostic.getKind() + ":\t Line [" + diagnostic.getLineNumber() + "] \t Position [" + diagnostic.getPosition() + "]\t" + diagnostic.getMessage(Locale.ROOT) + "\n");
        }

        return messages;
    }

    public boolean checkStartCodeChanges(String startCode, String solutionCode) {
        String[] startCodeLines = startCode.split("\\R");
        String[] solutionCodeLines = solutionCode.split("\\R");

        boolean containsStartCode = true;
        for (String startLine : startCodeLines) {
            boolean foundMatch = false;
            for (String solutionLine : solutionCodeLines) {
                if (startLine.trim().equals(solutionLine.trim())) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                containsStartCode = false;
                break;
            }
        }

        if (containsStartCode) {
            return true;
        } else {
            return false;
        }
    }

}
