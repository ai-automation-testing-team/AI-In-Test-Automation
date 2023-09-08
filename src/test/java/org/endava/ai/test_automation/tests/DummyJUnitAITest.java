package org.endava.ai.test_automation.tests;

import io.qameta.allure.Description;
import org.endava.ai.test_automation.annotations.AnalysisAI;
import org.endava.ai.test_automation.annotations.DescAI;
import org.endava.ai.test_automation.logic.ServiceExample;
import org.endava.ai.test_automation.service.junit_impl.JUnit5TestResultHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(JUnit5TestResultHandler.class)
public class DummyJUnitAITest {

	@Test
	@AnalysisAI
	@Description("Viksa")
	@DescAI(value = false)
	public void example1() {
		List<Float> list = List.of(1F, 3F, 5F, -40F);
		String sum = ServiceExample.sum(list);
		Assertions.assertEquals(sum, "9");
	}

}
