package org.endava.ai.test_automation.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.aeonbits.owner.ConfigCache;
import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.config.AIConfig;
import org.endava.ai.test_automation.model.MessageTemplate;
import org.endava.ai.test_automation.util.MethodCodeExtractor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class TestResultHandlerImpl implements TestResultHandler {

    private static OpenAiService openAiServiceInstance;

    private static OpenAiService getOpenAiServiceInstance() {
        if (openAiServiceInstance == null) {
            openAiServiceInstance = new OpenAiService(aiConfig.token());
        }
        return openAiServiceInstance;
    }

    private final MessageTemplate messageTemplate;
    protected static final AIConfig aiConfig = ConfigCache.getOrCreate(AIConfig.class);


    public TestResultHandlerImpl(String techUsed) {
        this.messageTemplate = new MessageTemplate(
            "Java, " + techUsed + ", " + aiConfig.techUsed(),
            aiConfig.responseLimit(),
            "Please try to do a good analysis and try to provide me some proposal solution for my problem.",
            aiConfig.additionalRequest(),
            logContent()
        );
    }


    protected String handleTestFailure(AnalysisAI analysisAI, Throwable throwable) {
        StringBuilder aiResponse = new StringBuilder();
        if (Objects.nonNull(analysisAI)) {
            List<String> methodCodes = MethodCodeExtractor.extractMethodCodes((Exception) throwable);
            String stackTrace = getStackTrace(throwable);
            String message = messageTemplate.formatMessage(throwable.getMessage(), stackTrace, methodCodes);


            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                message);
            messages.add(systemMessage);

            OpenAiService service = getOpenAiServiceInstance();

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(aiConfig.model())
                .messages(messages)
                .n(1)
                .maxTokens(Integer.valueOf(aiConfig.responseLimit()))
                .logitBias(new HashMap<>())
                .build();


            List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
            choices.forEach(choice -> aiResponse.append(choice.getMessage().getContent()));
        }
        return aiResponse.toString();
    }


    protected String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

}
