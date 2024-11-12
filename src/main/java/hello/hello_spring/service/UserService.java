package hello.hello_spring.service;

import hello.hello_spring.domain.User;
import hello.hello_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean login(String username, String password) {
        // username으로 사용자 조회 후 password 일치 여부 확인

        return userRepository.findByUsername(username).getPassword()
                .equals(password);

    }
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null) {
            throw new IllegalArgumentException("No Username");
        }
        else if (!existingUser.getPassword().equals(currentPassword)) {
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