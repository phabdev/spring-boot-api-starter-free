package com.phabdev.apistarter.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end test through the HTTP layer, using the real H2 database.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    private static final String TASKS = "/api/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReturns201WithLocationAndBody() throws Exception {
        mockMvc.perform(post(TASKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Build the free starter kit",
                                  "description": "Create the public open-core version",
                                  "priority": "HIGH"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(TASKS + "/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Build the free starter kit"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void createWithBlankTitleReturns400WithFieldErrors() throws Exception {
        mockMvc.perform(post(TASKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "title": "  " }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.path").value(TASKS))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("title"));
    }

    @Test
    void createWithInvalidEnumReturns400() throws Exception {
        mockMvc.perform(post(TASKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "title": "Bad priority", "priority": "URGENT" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed request body"));
    }

    @Test
    void getUnknownIdReturns404() throws Exception {
        mockMvc.perform(get(TASKS + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task with id 999999 not found"))
                .andExpect(jsonPath("$.path").value(TASKS + "/999999"));
    }

    @Test
    void getWithNonNumericIdReturns400() throws Exception {
        mockMvc.perform(get(TASKS + "/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void fullLifecycleCreateUpdateGetDelete() throws Exception {
        MvcResult created = mockMvc.perform(post(TASKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "title": "Lifecycle task" }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(created.getResponse().getContentAsString());
        long id = body.get("id").asLong();

        mockMvc.perform(put(TASKS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Lifecycle task (done)",
                                  "description": "Finished",
                                  "status": "DONE",
                                  "priority": "LOW"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Lifecycle task (done)"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.priority").value("LOW"));

        mockMvc.perform(get(TASKS + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Finished"));

        mockMvc.perform(delete(TASKS + "/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(TASKS + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMissingStatusReturns400() throws Exception {
        mockMvc.perform(put(TASKS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "title": "No status", "priority": "LOW" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("status"));
    }
}
