package test_task.entity;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Transient
    private Boolean isAvailable;

    @OneToMany(mappedBy = "poll", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderNumber")
    private List<Question> questions = new ArrayList<>();

    public Poll() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @JsonGetter("isAvailable")
    public Boolean isAvailable() {
        return LocalDate.now().isAfter(startDate) && LocalDate.now().isBefore(endDate);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
