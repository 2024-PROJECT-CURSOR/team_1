package hello.hello_spring.repository;

import hello.hello_spring.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
    User findByUsername(String username);
}
