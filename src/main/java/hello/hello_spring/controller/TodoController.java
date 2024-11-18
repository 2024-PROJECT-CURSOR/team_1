package hello.hello_spring.controller;

import hello.hello_spring.domain.User;
import hello.hello_spring.model.Todo;
import hello.hello_spring.repository.TodoRepository;
import hello.hello_spring.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:8080") // Adjust as needed
public class TodoController {
    @Autowired
    private TodoService todoService;
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodoById(@PathVariable String id) {
        todoService.deleteTodoById(id);
    }

    // 검색 API
    @GetMapping("/search")
    public List<Todo> searchTodos(@RequestParam String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title);
    }

    // 생성자 주입
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


}
