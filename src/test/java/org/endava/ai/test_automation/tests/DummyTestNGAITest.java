package org.endava.ai.test_automation.tests;

import static org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler.getMavenBaseDirectory;
import static org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler.getSourceFileFromTestClass;

import io.qameta.allure.Description;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.endava.ai.test_automation.annotations.AnalysisAI;
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
	public void afterSuite() throws IOException, GitAPIException {
		Git git = Git.open(new File(""));
		String currentBranch = git.getRepository().getFullBranch();
		long l = System.currentTimeMillis();
		String branchName = "ai-description-" + l;
		git.branchCreate().setName(branchName).call();
		git.checkout().setName(branchName).call();
		git.add().addFilepattern(".").call();
		git.commit().setMessage("AI suggested changes").call();

		String token = "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";
		UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(token, "");
		git.push().setCredentialsProvider(credentialsProvider).call();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://api.github.com/repos/sushelski/AI-In-Test-Automation/pulls");
		httpPost.addHeader("Authorization", "token " + token);

		String json = "{\"title\": \"Pull Request Title\",\"body\": \"Pull Request Description\",\"head\": \""
				+ branchName + "\",\"base\": \"" + currentBranch + "\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/vnd.github.v3+json");
		httpPost.setHeader("Content-type", "application/json");

		CloseableHttpResponse response = httpClient.execute(httpPost);

		System.out.println(response.getStatusLine());
		httpClient.close();
	}

}
