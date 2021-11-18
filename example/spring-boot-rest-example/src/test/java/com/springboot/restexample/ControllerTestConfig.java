package com.springboot.restexample;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({VersionFilter.class, ObjectMapperConfig.class})
public class ControllerTestConfig {
}
