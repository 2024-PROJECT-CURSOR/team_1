package hello.hello_spring.controller;
import hello.hello_spring.domain.User;
import hello.hello_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Map;

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
    @GetMapping("/changePW")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "user/changePW";
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
    @PostMapping("/changePW")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        // 비밀번호 변경 서비스 로직 호출
        try {
            boolean isChanged = userService.changePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok("변경 성공");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage()); // 400 상태 코드와 메시지 반환
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request 상태 코드 반환
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage(); // 예외 메시지 반환
    }



    // GET 요청: 사용자 정보를 가져옵니다.
    @GetMapping("/{username}")
    @ResponseBody
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

}
