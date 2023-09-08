package org.endava.ai.test_automation.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlBuilder {

    private static final String CONTENT_PLACEHOLDER = "${CONTENT}";
    private static final String DESCRIPTION_TEMPLATE = "<div class=\"description\">%s</div>";
    private static final String CODE_BLOCK_TEMPLATE = "<div class=\"code-block\"><pre class=\"code-content\">%s</pre></div>";

    private static String formatForHtml(String text) {
        return text.replace("\n", "<br>");
    }

    public static String buildHtmlFromContent(String content) throws IOException {
        String template = new String(Files.readAllBytes(Paths.get("src/main/resources/html/analysis.html")));

        StringBuilder dynamicContent = new StringBuilder();

        int previousEndIndex = 0;
        while (true) {
            int codeStartIndex = content.indexOf("CodeStart", previousEndIndex);
            int codeEndIndex = content.indexOf("CodeEnd", previousEndIndex);

            if (codeStartIndex == -1) {
                String remainingDescription = content.substring(previousEndIndex).trim();
                if (!remainingDescription.isEmpty()) {
                    dynamicContent.append(String.format(DESCRIPTION_TEMPLATE, formatForHtml(remainingDescription)));
                }
                break;
            }

            String description = content.substring(previousEndIndex, codeStartIndex).trim();
            String codeBlock = content.substring(codeStartIndex + "CodeStart".length(), codeEndIndex).trim();

            if (!description.isEmpty()) {
                dynamicContent.append(String.format(DESCRIPTION_TEMPLATE, formatForHtml(description)));
            }

            dynamicContent.append(String.format(CODE_BLOCK_TEMPLATE, codeBlock));
            previousEndIndex = codeEndIndex + "CodeEnd".length();
        }

        return template.replace(CONTENT_PLACEHOLDER, dynamicContent.toString());
    }

}
