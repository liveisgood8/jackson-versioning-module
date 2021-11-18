package com.springboot.restexample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExampleController.class)
@Import({ControllerTestConfig.class})
class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetExampleWhenFirstVersion() throws Exception {
        mockMvc.perform(
                        get("/example")
                                .header("Version-Info", "v1.0.0")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"surname\": \"Superhero\"}"));
    }

    @Test
    void testGetExampleWhenSecondVersion() throws Exception {
        mockMvc.perform(
                        get("/example")
                                .header("Version-Info", "v2.0.0")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\": \"Alex\", \"surname\": \"Superhero\"}"));
    }

    @Test
    void testGetExampleWhenThirdVersion() throws Exception {
        mockMvc.perform(
                        get("/example")
                                .header("Version-Info", "v3.0.0")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\": \"Alex\", \"surname\": \"Clarke\"}"));
    }
}