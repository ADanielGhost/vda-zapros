package org.polytech.zapros.service.qe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.service.correcter.CorrectingContradictionsService;
import org.polytech.zapros.service.validating.ValidatingQesService;

public abstract class QuasiExpertServiceImpl implements QuasiExpertService {

    private final ValidatingQesService validatingQesService;
    private final CorrectingContradictionsService correctingContradictionsService;

    public QuasiExpertServiceImpl(ValidatingQesService validatingQesService, CorrectingContradictionsService correctingContradictionsService) {
        this.validatingQesService = validatingQesService;
        this.correctingContradictionsService = correctingContradictionsService;
    }

    @Override
    public BuildingQesCheckResult buildQes(List<Answer> answerList, QuasiExpertConfig config, List<Criteria> criteriaList, Double threshold) {
        List<QuasiExpert> result = buildNotCheckedQes(answerList, config);

        if (!validatingQesService.isQesValid(result, config, threshold)) {
            return correctingContradictionsService.correct(answerList, result, config, criteriaList);
        }

        for (QuasiExpert qe: result) {
            qe.calculateRang(criteriaList);
        }

        return new BuildingQesCheckResult(true, result);
    }

    /**
     * Устанавливает все ответы для всех квазиэкспертов.
     * <p>
     * В случае, если нужен дополнительный квазиэксперт, создает его и
     * инициализирует начиная с противоречащей оценки.
     * @param answerList лист ответов, из которого мы формируем матрицы квазиэкспертов.
     * @param config
     * @return
     */
    private List<QuasiExpert> buildNotCheckedQes(List<Answer> answerList, QuasiExpertConfig config) {
        List<QuasiExpert> result = new ArrayList<>();
        result.add(new QuasiExpert(config));

        for (Answer answer: answerList) {

            List<Boolean> needNewQe = result.stream()
                .map(quasiExpert -> !quasiExpert.setOneQe(answer))
                .collect(Collectors.toList());

            if (!needNewQe.contains(false)) {
                QuasiExpert newQe = new QuasiExpert(config);
                newQe.setFirstAnswer(answer);
                newQe.setOneQe(answer);
                answerList.forEach(newQe::setOneQe);
                result.add(newQe);
            }
        }

        return result;
    }
}
