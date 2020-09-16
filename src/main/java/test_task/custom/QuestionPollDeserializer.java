package test_task.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import test_task.entity.Poll;
import test_task.repository.PollRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Кастомный десериализатор ссылки на опрос.
 */
public class QuestionPollDeserializer extends StdDeserializer<Poll> {
    @Autowired
    private PollRepository pollRepository;

    public QuestionPollDeserializer() {
        this(null);
    }

    public QuestionPollDeserializer(Class<Poll> t) {
        super(t);
    }

    @Override
    public Poll deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        UUID pollUUID = UUID.fromString(p.getText());
        Optional<Poll> poll = pollRepository.findById(pollUUID);
        return poll.orElse(null);
    }
}
