package com.todo.controller;

import com.todo.model.Todo;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")  // Allow all origins (for frontend access)
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // ✅ Method to send metric to Graphite
    private void sendMetric(String metricName) {
        try (Socket socket = new Socket("graphite", 2003);
             OutputStream out = socket.getOutputStream()) {
            String data = metricName + " 1 " + (System.currentTimeMillis() / 1000) + "\n";
            out.write(data.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Get all todos
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // ✅ Add a new todo
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        Todo saved = todoRepository.save(todo);
        sendMetric("todoapp.todo_added");
        return saved;
    }

    // ✅ Update a todo
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setTitle(todoDetails.getTitle());
        todo.setCompleted(todoDetails.isCompleted());
        Todo updated = todoRepository.save(todo);
        sendMetric("todoapp.todo_updated");
        return updated;
    }

    // ✅ Delete a todo
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
        sendMetric("todoapp.todo_deleted");
    }
}