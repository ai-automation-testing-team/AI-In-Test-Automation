package org.endava.ai.test_automation.tests;

import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.logic.ServiceExample;
import org.endava.ai.test_automation.service.TestResultHandler;
import org.endava.ai.test_automation.service.testNG_impl.TestNGTestResultHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class DummyTestNGAITest {

    private TestResultHandler testResultHandler;


    @BeforeClass
    public void beforeClass() {
        testResultHandler = new TestNGTestResultHandler() {
            @Override
            public String logContent() {
                //todo implement fetching the log file
                return super.logContent();
            }
        };
    }


    @Test
    @AnalysisAI
    public void example1() {
        List<Float> list = List.of(1F, 3F, 5F, -40F);
        String sum = ServiceExample.sum(list);
        Assert.assertEquals(sum, "9");
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String aiResults = testResultHandler.handleTestResult(result);

        System.out.println("The AI suggestion for fixing the test is: \n" + aiResults);
        //todo add ai results somewhere

    }

}
