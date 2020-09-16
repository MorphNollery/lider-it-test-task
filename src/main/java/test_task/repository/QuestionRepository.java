package test_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test_task.entity.Poll;
import test_task.entity.Question;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    boolean existsByPollAndOrderNumber(Poll poll, Long orderNumber);
}
