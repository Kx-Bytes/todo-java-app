package com.todo.controller;

import com.todo.model.Todo;
import com.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setup() {
        todoRepository.deleteAll();
    }

    @Test
    public void testAddTodo() throws Exception {
        Todo todo = new Todo(null, "Test Task", false);
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    public void testGetTodos() throws Exception {
        Todo todo = new Todo(null, "Sample Task", false);
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        Todo todo = todoRepository.save(new Todo(null, "Delete Me", false));

        mockMvc.perform(delete("/api/todos/" + todo.getId()))
                .andExpect(status().isOk());
    }
}