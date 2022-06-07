package org.polytech.zapros.service.main;

import java.util.List;

import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.alternative.AlternativeRankingResult;
import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Answer.AnswerType;
import org.polytech.zapros.bean.AnswerCheckResult;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.bean.ReplacedAnswer;

/**
 * Интерфейс сервиса для взаимодействия с клиентом.
 */
public interface VdaZaprosService {
    /**
     * Метод для получения первого вопроса.
     * @param criteriaList список критериев.
     * @return структуру, содержащую ответы пользователя и указатели на сравниваемые оценки.
     */
    AnswerCheckResult askFirst(List<Criteria> criteriaList);

    /** Метод для добавления ответа пользователя.
     * @param checkResult структура, содержащую ответы пользователя и указатели на сравниваемые оценки.
     * @param answerType ответ пользователя для сравнения оценок по указателям в checkResult;
     * @param config конфиг квазиэксперта.
     * @return структуру, содержащую ответы пользователя и указатели на сравниваемые оценки.
     */
    AnswerCheckResult addAnswer(AnswerCheckResult checkResult, AnswerType answerType);

    /**
     * Метод для построения квазиэкспертов исходя из оценок пользователя.
     * @param answerList лист ответов пользователя;
     * @param config конфиг квазиэксперта.
     * @return структуру, содержащую либо построенных квазиэкспертов, либо ответ для исправления противоречий.
     */
    BuildingQesCheckResult buildQes(List<Answer> answerList, QuasiExpertConfig config, List<Criteria> criteriaList, Double threshold);

    /**
     * Метод для замены ответа пользователя. Используется при исправлении противоречий.
     * @param checkResult - buildingQesCheckResult;
     * @param answerType - ответ пользователя для исправления оценки из checkResult.
     * @return лист ответов с исправленным противоречием.
     */
    ReplacedAnswer replaceAnswer(BuildingQesCheckResult checkResult, AnswerType answerType);

    /**
     * Метод для ранжирования альтернатив на основе постоенных квазиэкспертов.
     * @param qes лист квазиэкспертов;
     * @param alternativeList лист альтернатив.
     * @return список ранжированных альтернатив.
     */
    AlternativeRankingResult rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config);
}
