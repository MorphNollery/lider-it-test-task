package test_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test_task.entity.Poll;

import java.util.UUID;

public interface PollRepository extends JpaRepository<Poll, UUID> {
}
