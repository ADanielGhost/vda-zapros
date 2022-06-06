package org.polytech.zapros.service.correcter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Answer.AnswerAuthor;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.bean.SuggestedAnswer;
import org.springframework.stereotype.Component;

@Component
public class CorrectingContradictionsServiceImpl implements CorrectingContradictionsService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public BuildingQesCheckResult correct(List<Answer> answerList, List<QuasiExpert> qes, QuasiExpertConfig config, List<Criteria> criteriaList) {
        log.info("correcting started");

        int[][] matrixContradictions = calculateMatrixContradictions(qes, config);
        Map<Assessment, Integer> mapContradictions = calculateMapContradictions(matrixContradictions, criteriaList);
        //displayContradictions(matrixContradictions, mapContradictions, config, criteriaList);
        Answer answerForReplacing = findMostControversialAnswer(mapContradictions, answerList, qes, criteriaList);

        BuildingQesCheckResult result = new BuildingQesCheckResult();
        result.setOver(false);
        result.setAnswerForReplacing(answerForReplacing);
        result.setAnswerList(answerList);

        log.info("correcting finished with " + answerForReplacing);
        return result;
    }

    private Map<Assessment, Integer> calculateMapContradictions(int[][] matrixContradictions, List<Criteria> criteriaList) {
        Map<Assessment, Integer> result = new HashMap<>();

        for (int i = 0; i < matrixContradictions.length; i++) {
            result.put(getAssessmentByOrderId(i, criteriaList), Arrays.stream(matrixContradictions[i]).reduce(0, Integer::sum));
        }
        return result;
    }

    private int[][] calculateMatrixContradictions(List<QuasiExpert> qes, QuasiExpertConfig config) {
        int[][] result = new int[config.getLen()][config.getLen()];

        for (int i = 0; i < config.getLen(); i++) {
            for (int j = 0; j < config.getLen(); j++) {
                int value = qes.get(0).getMatrix()[i][j];

                for (int k = 1; k < qes.size(); k++) {
                    if (qes.get(k).getMatrix()[i][j] != value) {
                        result[i][j] = 1;
                        break;
                    }
                }
            }
        }

        return result;
    }

    private Answer findMostControversialAnswer(Map<Assessment, Integer> mapContradictions, List<Answer> answerList, List<QuasiExpert> qes, List<Criteria> criteriaList) {

        SuggestedAnswer suggestedAnswer = findMax(mapContradictions);
        Assessment first = suggestedAnswer.getI();

        List<Answer> wrongAnswerList = qes.stream()
            .map(QuasiExpert::getFirstAnswer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        for (Assessment second: suggestedAnswer.getJ()) {
            Optional<Answer> answer = wrongAnswerList.stream()
                .filter(x -> (x.getI().equals(first) && x.getJ().equals(second)) || (x.getI().equals(second) && x.getJ().equals(first)))
                .filter(x -> x.getAnswerAuthor() != AnswerAuthor.REPLACED)
                .findFirst();

            if (answer.isPresent()) {
                return answer.get();
            }
        }

        for (Assessment second: suggestedAnswer.getJ()) {
            Optional<Answer> answer = answerList.stream()
                .filter(x -> (x.getI() == first && x.getJ() == second) || (x.getI() == second && x.getJ() == first))
                .filter(x -> x.getAnswerAuthor() != AnswerAuthor.REPLACED)
                .findFirst();

            if (answer.isPresent()) {
                return answer.get();
            }
        }

        throw new IllegalStateException("CANNOT FIND ANSWER FOR REPLACE!!!");
    }

    private SuggestedAnswer findMax(Map<Assessment, Integer> mapContradictions) {
        Map.Entry<Assessment, Integer> firstMaxContradiction = mapContradictions.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new IllegalStateException("findMostControversialAnswer"));

        List<Assessment> listMostContradictions = mapContradictions.entrySet().stream()
            .filter(x -> x.getKey().getCriteriaId() != firstMaxContradiction.getKey().getCriteriaId())
            .filter(x -> x.getValue().equals(firstMaxContradiction.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (!listMostContradictions.isEmpty()) {
            SuggestedAnswer result = new SuggestedAnswer();
            result.setI(firstMaxContradiction.getKey());
            result.setJ(listMostContradictions);
            return result;
        }

        Map.Entry<Assessment, Integer> secondMaxContradiction = mapContradictions.entrySet().stream()
            .filter(x -> x.getKey().getCriteriaId() != firstMaxContradiction.getKey().getCriteriaId())
            .max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new IllegalStateException("findSecondControversialAnswer"));

        List<Assessment> listSecondMostValues = mapContradictions.entrySet().stream()
            .filter(x -> x.getKey().getCriteriaId() != firstMaxContradiction.getKey().getCriteriaId())
            .filter(x -> x.getValue().equals(secondMaxContradiction.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        SuggestedAnswer result = new SuggestedAnswer();
        result.setI(firstMaxContradiction.getKey());
        result.setJ(listSecondMostValues);
        return result;
    }

    private void displayContradictions(int[][] matrixContradictions, Map<Assessment, Integer> mapContradictions, QuasiExpertConfig config, List<Criteria> criteriaList) {
        log.info("Матрица противоречий: ");
        for (int i = 0; i < config.getLen(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < config.getLen(); j++) {
                sb.append(matrixContradictions[i][j]).append(" ");
            }
            log.info(sb + "-> " + mapContradictions.get(getAssessmentByOrderId(i, criteriaList)));
        }
        log.info("!!!");
    }

    private Assessment getAssessmentByOrderId(int orderId, List<Criteria> criteriaList) {
        for (Criteria c: criteriaList) {
            for (Assessment a: c.getAssessments()) {
                if (orderId == a.getOrderId()) return a;
            }
        }
        // невозможный кейс
        throw new IllegalStateException("!!! getAssessmentByOrderId failed");
    }
}
