package org.endava.ai.test_automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PullRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("head")
    private String head;

    @JsonProperty("base")
    private String base;

    public PullRequest(String title, String body, String head, String base) {
        this.title = title;
        this.body = body;
        this.head = head;
        this.base = base;
    }
}
