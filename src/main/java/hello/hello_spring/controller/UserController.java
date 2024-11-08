package hello.hello_spring.controller;
import hello.hello_spring.domain.User;
import hello.hello_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET 요청: 회원가입 폼을 보여줍니다.
    @GetMapping("/signUp")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "user/signUp"; // templates/user/signUp.html로 이동
    }
    @GetMapping("/signIn")
    public String showSignInForm(Model model) {
        model.addAttribute("user", new User());
        return "user/signIn"; // templates/user/signUp.html로 이동
    }
    // 로그인 요청을 처리
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = userService.login(username, password);

        return isAuthenticated ? "로그인 성공" : "로그인 실패";
    }

    // POST 요청: 회원가입 요청을 처리합니다.
    @PostMapping("/register")
    @ResponseBody
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // GET 요청: 사용자 정보를 가져옵니다.
    @GetMapping("/{username}")
    @ResponseBody
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

}
