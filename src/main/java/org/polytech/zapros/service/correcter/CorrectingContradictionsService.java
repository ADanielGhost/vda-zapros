package org.polytech.zapros.service.correcter;

import java.util.List;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;

/**
 * Сервис для устранения противоречий.
 */
public interface CorrectingContradictionsService {

    /**
     * Метод для замены одного ответа. Находит ответ с самым большим кол-вом противоречий и заменяет его.
     * @param answerList лист ответов;
     * @param qes список квазиэкспертов;
     * @param config конфиг квазиэксперта.
     * @return список ответов.
     */
    BuildingQesCheckResult correct(List<Answer> answerList, List<QuasiExpert> qes, QuasiExpertConfig config, List<Criteria> criteriaList);
}
