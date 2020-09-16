package test_task.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import test_task.entity.Poll;

import java.io.IOException;

/**
 * Кастомный сериализатор ссылки на опрос.
 */
public class QuestionPollSerializer extends StdSerializer<Poll> {
    public QuestionPollSerializer() {
        this(null);
    }

    public QuestionPollSerializer(Class<Poll> t) {
        super(t);
    }

    @Override
    public void serialize(Poll poll, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(poll.getId());
    }
}
