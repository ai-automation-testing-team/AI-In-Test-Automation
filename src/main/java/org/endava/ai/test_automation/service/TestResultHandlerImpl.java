package org.endava.ai.test_automation.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.aeonbits.owner.ConfigCache;
import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.annotations.DescAI;
import org.endava.ai.test_automation.annotations.FixAI;
import org.endava.ai.test_automation.config.AIConfig;
import org.endava.ai.test_automation.model.DescUser;
import org.endava.ai.test_automation.model.MessageTemplate;
import org.endava.ai.test_automation.util.MethodCodeExtractor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.endava.ai.test_automation.util.MethodCodeExtractor.getFullMethodCode;

public abstract class TestResultHandlerImpl implements TestResultHandler {

    protected static final AIConfig aiConfig = ConfigCache.getOrCreate(AIConfig.class);


    private static OpenAiService openAiServiceInstance;


    private static OpenAiService getOpenAiServiceInstance() {
        if (openAiServiceInstance == null) {
            openAiServiceInstance = new OpenAiService(aiConfig.token(), Duration.ofSeconds(50L));
        }
        return openAiServiceInstance;
    }


    private final MessageTemplate messageTemplate;


    public TestResultHandlerImpl(String techUsed) {
        this.messageTemplate = new MessageTemplate(
            "Java, " + techUsed + ", " + aiConfig.techUsed(),
            aiConfig.responseLimit(),
            aiConfig.additionalRequest(),
            logContent()
        );
    }


    protected String handleTestAnalysis(AnalysisAI analysisAI, Throwable throwable) {
        StringBuilder aiResponse = new StringBuilder();
        if (Objects.nonNull(analysisAI)) {
            List<String> methodCodes = MethodCodeExtractor.extractMethodCodes((Exception) throwable);
            String stackTrace = getStackTrace(throwable);
            String message = messageTemplate.formatMessageAnalysis(throwable.getMessage(), stackTrace, methodCodes);


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
                .maxTokens(350)
                .logitBias(new HashMap<>())
                .build();


            List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
            choices.forEach(choice -> aiResponse.append(choice.getMessage().getContent()));
        }
        return aiResponse.toString();
    }


    protected String handleTestFix(FixAI fixAI, Throwable throwable) {
        StringBuilder aiResponse = new StringBuilder();
        if (Objects.nonNull(fixAI)) {
            List<String> methodCodes = MethodCodeExtractor.extractMethodCodes((Exception) throwable);
            String stackTrace = getStackTrace(throwable);
            String message = messageTemplate.formatMessageFixTest(throwable.getMessage(), stackTrace, methodCodes);


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
                .maxTokens(350)
                .logitBias(new HashMap<>())
                .build();


            List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
            choices.forEach(choice -> aiResponse.append(choice.getMessage().getContent()));
        }
        return aiResponse.toString();
    }


    protected String handleTestDescription(DescAI descAI, String className, String testMethodName) {
        StringBuilder aiResponse = new StringBuilder();
        Class<?> clazz;
        String code;
        if (Objects.nonNull(descAI) && descAI.value()) {
            try {
                clazz = Class.forName(className);
                code = getFullMethodCode(clazz.getName(), testMethodName);

            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }


            String message = messageTemplate.formatMessageDescription(code,
                DescUser.valueOf(aiConfig.descriptionRole()));


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
                .maxTokens(350)
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
