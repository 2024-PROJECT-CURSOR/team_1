package hello.hello_spring.controller;

import hello.hello_spring.model.Todo;
import hello.hello_spring.service.TodoService;
import hello.hello_spring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:8080")  // 프론트엔드 서버가 8081 포트에서 실행 중이라면
@RestController
@RequestMapping("/api/todos")// 프론트엔드 서버와 연결 허용
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private JwtUtil jwtUtil;

    // 로그인한 사용자의 ID로 TODO 리스트 조회
    @GetMapping
    public List<Todo> getTodos(@RequestHeader("Authorization") String authToken) {
        // JWT에서 사용자 ID 추출
        String userId = getUserIdFromJwt(authToken);

        // 사용자 ID로 TODO 리스트 가져오기
        return todoService.getTodosByUserId(userId);
    }

    // JWT에서 사용자 ID 추출하는 메서드
    private String getUserIdFromJwt(String authToken) {
        // JWT에서 사용자 ID를 추출하는 로직 (예: JWT 파싱)
        return jwtUtil.extractUsername(authToken); // jwtUtil에서 사용자 ID 추출
    }

    // TODO 항목 생성
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo, @RequestHeader("Authorization") String authToken) {
        // JWT에서 사용자 ID 추출
        String userId = getUserIdFromJwt(authToken);
        todo.setUserId(userId);  // 사용자 ID를 설정하여 저장
        return todoService.createTodo(todo);
    }

    // TODO 항목 수정
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    // TODO 항목 삭제
    @DeleteMapping("/{id}")
    public void deleteTodoById(@PathVariable String id) {
        todoService.deleteTodoById(id);
    }

}