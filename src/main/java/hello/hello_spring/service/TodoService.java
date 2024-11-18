package hello.hello_spring.service;
import hello.hello_spring.model.Todo;
import hello.hello_spring.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(String id) {
        return todoRepository.findById(id).orElse(null);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(String id, Todo todo) {
        if (todoRepository.existsById(id)) {
            todo.setId(id);
            return todoRepository.save(todo);
        }
        return null;
    }

    public void deleteTodoById(String id) {
        todoRepository.deleteById(id);
    }
}