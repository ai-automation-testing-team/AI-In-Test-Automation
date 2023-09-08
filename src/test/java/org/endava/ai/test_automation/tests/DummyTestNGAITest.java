package org.endava.ai.test_automation.tests;

import io.qameta.allure.Description;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.annotations.DescAI;
import org.endava.ai.test_automation.logic.ServiceExample;
import org.endava.ai.test_automation.service.TestModifier;
import org.endava.ai.test_automation.service.TestResultHandler;
import org.endava.ai.test_automation.service.testNG_impl.TestNGTestResultHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler.getMavenBaseDirectory;
import static org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler.getSourceFileFromTestClass;

public class DummyTestNGAITest {

    private TestResultHandler testResultHandler;


    @BeforeClass
    public void beforeClass() {

        testResultHandler = new TestNGTestResultHandler() {
            @Override
            public String logContent() {
                // todo implement fetching the log file
                return super.logContent();

            }
        };
    }


    @Test
    @AnalysisAI
    @Description("Viksa")
    @DescAI()
    public void example1() {
        List<Float> list = List.of(1F, 3F, 5F, -40F);
        String sum = ServiceExample.sum(list);
        Assert.assertEquals(sum, "9");
    }


    @Test
    @AnalysisAI
    @Description("Viksa")
    public void example2() {
        List<Float> list = List.of(1F, 3F, 5F, 40F);
        String sum = ServiceExample.sum(list);
        Assert.assertEquals(sum, "49");

    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {


        String aiResults = testResultHandler.handleTestResult(result);



        if(result.getStatus() == ITestResult.SUCCESS){
            Class<?> aClass = result.getTestClass().getRealClass();
            String mavenBaseDirectory = getMavenBaseDirectory();
            File file = getSourceFileFromTestClass(aClass, mavenBaseDirectory);
            String description = testResultHandler.handleDescription(result);
            TestModifier.addDescriptionAnnotation(file, result.getMethod().getMethodName(), description);
        }




        // System.out.println("The AI suggestion for fixing the test is: \n" + aiResults);
        // todo add ai results somewhere

    }


    @AfterSuite(alwaysRun = true)
    public void afterSuite() throws IOException, GitAPIException {
        Git git = Git.open(new File(
            "C:\\Users\\vsushelski\\OneDrive - ENDAVA\\Documents\\Private\\Repositories\\AI-In-Test-Automation.git"));
        long l = System.currentTimeMillis();
        String branchName = "ai-description-" + l;
        git.branchCreate().setName(branchName).call();
        git.checkout().setName(branchName).call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("AI suggested changes").call();
        UsernamePasswordCredentialsProvider credentialsProvider = new
            UsernamePasswordCredentialsProvider("vsushelski",
            "Replace_me");
        git.push().setCredentialsProvider(credentialsProvider).call();


    }

}
