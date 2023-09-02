package org.endava.ai.test_automation.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:ai-config.properties")
public
interface AIConfig extends Config {

    @Key("token")
    String token();

    @Key("model")
    String model();

    @Key("tech.used")
    String techUsed();

    @Key("response.limit")
    String responseLimit();

    @Key("additional.request")
    String additionalRequest();

}
