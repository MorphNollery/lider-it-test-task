package test_task.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import test_task.custom.QuestionPollDeserializer;
import test_task.custom.QuestionPollSerializer;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = QuestionPollSerializer.class)
    @JsonDeserialize(using = QuestionPollDeserializer.class)
    private Poll poll;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private Integer orderNumber;

    public Question() {
    }

    public UUID getId() {
        return id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
