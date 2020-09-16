package test_task;

import test_task.entity.Poll;

import java.util.Comparator;

public enum PollsSort {
    NAME((o1, o2) -> {
        return o1.getName().compareTo(o2.getName());
    }),
    START_DATE((o1, o2) -> {
        return o1.getStartDate().compareTo(o2.getStartDate());
    });

    Comparator<Poll> comparator;

    PollsSort(Comparator<Poll> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Poll> getComparator() {
        return comparator;
    }
}
