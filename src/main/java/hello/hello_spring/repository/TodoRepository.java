package hello.hello_spring.repository;

import hello.hello_spring.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    List<Todo> findByTitleContainingIgnoreCase(String title);
}

