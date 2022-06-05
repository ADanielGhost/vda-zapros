package org.polytech.zapros.service.answer;

import java.util.List;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.AnswerCheckResult;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.bean.ReplacedAnswer;

/**
 * Сервис для работы с ответами.
 * <p>
 * Спрашивает ответы у пользователя, а также добавляет и заменяет их.
 */
public interface AnswerService {

    /**
     * Смотри {@link org.polytech.zapros.service.main.VdaZaprosService#askFirst}
     */
    AnswerCheckResult askFirst(List<Criteria> criteriaList);

    /**
     * Смотри {@link org.polytech.zapros.service.main.VdaZaprosService#addAnswer}
     */
    AnswerCheckResult addAnswer(AnswerCheckResult checkResult, Answer.AnswerType answerType);

    /**
     * Смотри {@link org.polytech.zapros.service.main.VdaZaprosService#replaceAnswer}
     */
    ReplacedAnswer replaceAnswer(BuildingQesCheckResult checkResult, Answer.AnswerType answerType);
}
