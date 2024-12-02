package hello.hello_spring.service;

import hello.hello_spring.model.User;
import hello.hello_spring.repository.UserRepository;
import hello.hello_spring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil; // JwtUtil 객체 주입

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return jwtUtil.generateToken(username); // 로그인 성공 시 JWT 토큰 발급
        }

        throw new IllegalArgumentException("Invalid username or password");
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User existingUser = userRepository.findByUsername(username);
        System.out.println(existingUser + " "+ username + " " + currentPassword + " " + newPassword);
        if (existingUser == null) {
            throw new IllegalArgumentException("No Username");
        } else if (!existingUser.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("Wrong currentPassword");
        }
        try {
            existingUser.setPassword(newPassword);
            userRepository.save(existingUser);  // 변경된 사용자 정보 저장
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public User registerUser(User user) {
        // 중복된 사용자 이름 체크
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}