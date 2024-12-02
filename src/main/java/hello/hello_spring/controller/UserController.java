package hello.hello_spring.controller;

import hello.hello_spring.model.User;
import hello.hello_spring.service.UserService;
import hello.hello_spring.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private JwtUtil jwtUtil;

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
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        try {
            String token = userService.login(username, password);
            return ResponseEntity.ok(token); // JWT 토큰 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST 요청: 회원가입 요청을 처리합니다.
    @PostMapping("/register")
    @ResponseBody
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/changePW")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 유효하지 않습니다.");
        }

        token = token.substring(7); // "Bearer " 부분을 제외한 실제 토큰 추출

        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        System.out.println("Token: " + token);  // 토큰을 확인

        // JWT에서 사용자 이름 추출
        String username = jwtUtil.extractUsername(token);
        System.out.println("username = " + username);


        // 비밀번호 변경 서비스 로직 호출
        try {
            boolean isChanged = userService.changePassword(username, currentPassword, newPassword);
            System.out.println("잘됨");
            return ResponseEntity.ok("변경 성공");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.ok(ex.getMessage()); // 400 상태 코드와 메시지 반환
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

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        token = token.substring(7);

        try {
            // Try extracting username from the token
            String username = jwtUtil.extractUsername(token);

            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Missing username in token
            }

            // Retrieve user by username
            User user = userService.getUserByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // User not found
            }

            return ResponseEntity.ok(user);  // Return user profile

        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // 500 Internal Server Error
        }
    }


}