package test_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import test_task.PollsSort;
import test_task.entity.Poll;
import test_task.entity.Question;
import test_task.repository.PollRepository;
import test_task.repository.QuestionRepository;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/polls")
public class PollController {
    private PollRepository pollRepository;
    private QuestionRepository questionRepository;

    @Autowired
    private PollController(PollRepository pollRepository, QuestionRepository questionRepository) {
        this.pollRepository = pollRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Возвращает все опросы, имеющиеся в базе данных.
     * @param sort вариант сортировки {@link PollsSort}
     * @return список опросов
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Poll> allPolls(@RequestParam PollsSort sort) {
        List<Poll> polls = pollRepository.findAll();
        polls.sort(sort.getComparator());
        return polls;
    }

    /**
     * Добавляет в базу данных новый опрос и вопросы опроса.
     * @param newPoll - новый опрос
     * @return опрос и вопросы с указанными идентификаторами.
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Poll addPoll(@RequestBody Poll newPoll) {
        Poll poll = pollRepository.save(newPoll);
        List<Integer> pollQuestionsOrderNumbers = new ArrayList<>();

        for (Question newPollQuestion : newPoll.getQuestions()) { // Вероятно, не самое хорошее решение, но лучше ничего не придумал;
            changeOrderNumberIfTaken(pollQuestionsOrderNumbers, newPollQuestion);
            newPollQuestion.setPoll(poll);
        }
        questionRepository.saveAll(newPoll.getQuestions());

        return poll;
    }

    /**
     * Редактирует опрос и вопросы опроса. Идентификаторы изменяемого опроса и изменяемых вопросов должны быть указаны и не изменяться.
     * Если идентификатор вопроса не указан, вопрос считается новым.
     * @param editedPoll - измененный опрос.
     * @return null, если не указан идентификатор опроса или опрос с таким идентификатором не найден, иначе возвращает измененный опрос.
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Poll editQuestion(@RequestBody Poll editedPoll) {
        if (editedPoll.getId() == null) {
            return null;
        }
        Optional<Poll> originalPoll = pollRepository.findById(editedPoll.getId());

        if (originalPoll.isPresent()) {
            List<UUID> originalPollQuestionsUUIDs = originalPoll.get().getQuestions()
                    .stream()
                    .map(Question::getId)
                    .collect(Collectors.toList());

            List<Integer> pollQuestionsOrderNumbers = originalPoll.get().getQuestions()
                    .stream()
                    .map(Question::getOrderNumber)
                    .collect(Collectors.toList());

            for (Question editedPollQuestion : editedPoll.getQuestions()) {
                if (editedPollQuestion.getId() != null) {
                    originalPollQuestionsUUIDs.remove(editedPollQuestion.getId());
                    editedPollQuestion.setPoll(originalPoll.get());
                } else {
                    changeOrderNumberIfTaken(pollQuestionsOrderNumbers, editedPollQuestion);
                }
                questionRepository.save(editedPollQuestion);
            }

            originalPollQuestionsUUIDs.forEach(questionUUID -> questionRepository.deleteById(questionUUID));

            return pollRepository.save(editedPoll);
        }
        return null;
    }

    /**
     * Удаляет опрос и привязанные к нему вопросы.
     * @param poll удаляемый опрос
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    public void deleteQuestion(@RequestBody Poll poll) {
        pollRepository.delete(poll);
    }


    /**
     * Увеличивает на единицу порядковый номер вопроса в опросе, пока все номера не будут уникальными.
     * @param pollQuestionsOrderNumbers список занятых в опросе порядковых номеров
     * @param question вопрос опроса
     */
    private void changeOrderNumberIfTaken(List<Integer> pollQuestionsOrderNumbers, Question question) {
        Integer editedPollNewQuestionOrderNumber = question.getOrderNumber();
        boolean changed = false;

        while (pollQuestionsOrderNumbers.contains(editedPollNewQuestionOrderNumber)) {
            editedPollNewQuestionOrderNumber++;
            changed = true;
        }
        if (changed) {
            question.setOrderNumber(editedPollNewQuestionOrderNumber);
        }
        pollQuestionsOrderNumbers.add(editedPollNewQuestionOrderNumber);
    }

}
