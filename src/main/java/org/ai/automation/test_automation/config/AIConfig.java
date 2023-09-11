package org.ai.automation.test_automation.config;

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

    @Key("forbidden.packages")
    String forbiddenPackages();

    @Key("project.package")
    String projectPackage();

    @Key("send.context")
    String sendContext();

    @Key("description.role")
    String descriptionRole();



}
