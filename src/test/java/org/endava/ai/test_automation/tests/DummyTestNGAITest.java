package org.endava.ai.test_automation.tests;

import static org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler.getMavenBaseDirectory;
import static org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler.getSourceFileFromTestClass;

import io.qameta.allure.Description;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.aeonbits.owner.ConfigCache;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.config.AIConfig;
import org.endava.ai.test_automation.logic.ServiceExample;
import org.endava.ai.test_automation.service.TestModifier;
import org.endava.ai.test_automation.service.TestResultHandler;
import org.endava.ai.test_automation.service.testNG_impl.TestNGTestResultHandler;
import org.endava.ai.test_automation.util.GitOperations;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DummyTestNGAITest {

	private TestResultHandler testResultHandler;
	protected static final AIConfig aiConfig = ConfigCache.getOrCreate(AIConfig.class);

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
	public void example1() {
		List<Float> list = List.of(1F, 3F, 5F, -40F);
		String sum = ServiceExample.sum(list);
		Assert.assertEquals(sum, "9");
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) {
		// String aiResults = testResultHandler.handleTestResult(result);

		Class<?> aClass = result.getTestClass().getRealClass();
		String mavenBaseDirectory = getMavenBaseDirectory();
		File file = getSourceFileFromTestClass(aClass, mavenBaseDirectory);
		TestModifier.addDescriptionAnnotation(file, result.getMethod().getMethodName(), "Viksa");

		// System.out.println("The AI suggestion for fixing the test is: \n" +
		// aiResults);
		// todo add ai results somewhere

	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws IOException {
		Git git = Git.open(new File(""));
		String currentBranch = git.getRepository().getFullBranch();
		long l = System.currentTimeMillis();
		String newBranchName = "ai-description-" + l;
		String repoPath = "";
		String token = aiConfig.tokenFirstPart() + aiConfig.tokenSecondPart();
		String repoOwner = "sushelski";
		String repoName = "AI-In-Test-Automation";
		String title = "Pull Request AI";
		String description = "Pull Request Description AI";

		GitOperations gitOps = new GitOperations();

		try {
			gitOps.createBranchAndCommit(repoPath, newBranchName);
			gitOps.pushToRemote(repoPath, token);
			gitOps.createPullRequest(repoOwner, repoName, title, description, newBranchName, currentBranch, token);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	}

}
