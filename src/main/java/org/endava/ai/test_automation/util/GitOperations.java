package org.endava.ai.test_automation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import org.endava.ai.test_automation.model.PullRequest;

public class GitOperations {

    public void createBranchAndCommit(String repoPath, String branchName) throws IOException, GitAPIException {
        Git git = Git.open(new File(repoPath));
        git.branchCreate().setName(branchName).call();
        git.checkout().setName(branchName).call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("AI suggested changes").call();
    }

    public void pushToRemote(String repoPath, String token) throws GitAPIException, IOException {
        Git git = Git.open(new File(repoPath));
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(token, "");
        git.push().setCredentialsProvider(credentialsProvider).call();
    }

    public void createPullRequest(String repoOwner, String repoName, String title, String description, String branchName, String baseBranch, String token)
        throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.github.com/repos/" + repoOwner + "/" + repoName + "/pulls");
        httpPost.addHeader("Authorization", "token " + token);

        PullRequest pullRequest =
            new PullRequest(title, description, branchName, baseBranch);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(pullRequest);

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/vnd.github.v3+json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = httpClient.execute(httpPost);

        System.out.println(response.getStatusLine());
        httpClient.close();
    }
}
