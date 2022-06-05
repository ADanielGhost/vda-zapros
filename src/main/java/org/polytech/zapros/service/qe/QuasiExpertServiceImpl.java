package org.polytech.zapros.service.qe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Assessment;
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

        result.forEach(x -> calculateRang(x, criteriaList, config));

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
        result.add(initNewQuasiExpert(config));

        for (Answer answer: answerList) {

            List<Boolean> needNewQe = result.stream()
                .map(quasiExpert -> !setOneAnswer(quasiExpert, answer, config))
                .collect(Collectors.toList());

            if (!needNewQe.contains(false)) {
                QuasiExpert newQe = initNewQuasiExpert(config);
                newQe.setFirstAnswer(answer);
                setOneAnswer(newQe, answer, config);
                answerList.forEach(x -> setOneAnswer(newQe, x, config));
                result.add(newQe);
            }
        }

        return result;
    }

    public QuasiExpert initNewQuasiExpert(QuasiExpertConfig config) {
        int[][] matrix = new int[config.getLen()][config.getLen()];
        for (int i = 0; i < config.getLen(); i++) {
            System.arraycopy(
                config.getInitData()[i], 0,
                matrix[i], 0,
                config.getLen()
            );
        }

        QuasiExpert result = new QuasiExpert();
        result.setMatrix(matrix);
        return result;
    }

    public boolean setOneAnswer(QuasiExpert quasiExpert, Answer answer, QuasiExpertConfig config) {
        switch (answer.getAnswerType()) {
            case BETTER: return setOneAnswer(answer.getI().getOrderId(), answer.getJ().getOrderId(), quasiExpert, config);
            case WORSE: return setOneAnswer(answer.getJ().getOrderId(), answer.getI().getOrderId(), quasiExpert, config);
            case EQUAL: throw new IllegalStateException("CANNOT ADD EQUAL ANSWER!!!");
            default: throw new IllegalStateException("Unexpected value: TODO" + answer.getAnswerType());
        }
    }

    /**
     * Данный метод устанавливает ответ пары {@code (i, j)}
     * в данную матрицу. Также устанавливает все вытекающие ответы,
     * исходя из ответа {@code (i, j)}.
     *
     * @param quasiExpert
     * @param i один из ответов.
     * @param j другой из ответов.
     * @return {@code true}, если противоречий нет;
     * {@code false}, если противоречия есть (требуется новый квазиэксперт).
     */
    private boolean setOneAnswer(int i, int j, QuasiExpert quasiExpert, QuasiExpertConfig config) {
        if (quasiExpert.getMatrix()[j][i] == 1) return false;

        quasiExpert.getMatrix()[i][j] = 1;

        // проверка на оценки хуже
        for (int k = 0; k < config.getLen(); k++) {
            if ((k == j) || (config.getIndexes().contains(k))) continue;

            if (quasiExpert.getMatrix()[j][k] == 1) {
                setOneAnswer(i, k, quasiExpert, config);
            }
        }

        // проверка на оценки лучше
        for (int k = 0; k < config.getLen(); k++) {
            if ((k == i) || (config.getIndexes().contains(k))) continue;

            if (quasiExpert.getMatrix()[k][i] == 1) {
                setOneAnswer(k, j, quasiExpert, config);
            }
        }

        return true;
    }

    /**
     * Данный метод присваивает ранги оценкам.
     */
    public void calculateRang(QuasiExpert quasiExpert, List<Criteria> criteriaList, QuasiExpertConfig config) {
        int cur = 0;
        Map<Assessment, Integer> ranks = new HashMap<>();

        for (Criteria criteria: criteriaList) {
            for (Assessment assessment: criteria.getAssessments()) {
                int rank = 1;
                for (int i = 0; i < config.getLen(); i++) {
                    if (quasiExpert.getMatrix()[cur][i] != 1) {
                        rank++;
                    }
                }
                cur++;

                ranks.put(assessment, rank);
            }
        }
        quasiExpert.setRanks(ranks);
    }
}
