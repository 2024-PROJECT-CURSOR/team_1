
package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodolistShowController {

    @GetMapping("/todolist")
    public String showTodoListPage(Model model) {
        // 필요하다면 모델에 데이터 추가
        return "main/todolist"; // src/main/resources/templates/main/todolist.html 로 이동
    }
}