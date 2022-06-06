package org.polytech.zapros.service.buildqe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.springframework.stereotype.Component;

@Component
public class BuildQesServiceImpl implements BuildQesService {

    private static class Point {
        int i;
        int j;

        public static Point of(int i, int j) {
            Point point = new Point();
            point.i = i;
            point.j = j;
            return point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return i == point.i && j == point.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    @Override
    public List<QuasiExpert> build(List<Answer> answerList, List<Criteria> criteriaList, QuasiExpertConfig config) {
        List<QuasiExpert> result = new ArrayList<>();
        result.add(initNewQuasiExpert(config));

        for (Answer answer: answerList) {

            List<Boolean> needNewQe = result.stream()
                .map(quasiExpert -> !setOneAnswer(answer, quasiExpert, criteriaList, config))
                .collect(Collectors.toList());

            // если есть противоречия, строим еще одного квазиэксперта, но с противоречащего ответа
            if (!needNewQe.contains(false)) {
                QuasiExpert newQe = initNewQuasiExpert(config);
                newQe.setFirstAnswer(answer);
                setOneAnswer(answer, newQe, criteriaList, config);
                answerList.forEach(x -> setOneAnswer(x, newQe, criteriaList, config));
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

    public boolean setOneAnswer(Answer answer, QuasiExpert quasiExpert, List<Criteria> criteriaList, QuasiExpertConfig config) {
        System.out.println();
        switch (answer.getAnswerType()) {
            case BETTER: return setOneAnswer(answer.getI().getOrderId(), answer.getJ().getOrderId(), 0, 0, false, new ArrayList<>(), quasiExpert, criteriaList, config);
            case WORSE: return setOneAnswer(answer.getJ().getOrderId(), answer.getI().getOrderId(), 0, 0, false, new ArrayList<>(), quasiExpert, criteriaList, config);
            case EQUAL: {
                // A2 = B2 <=> A2 > B2 && B2 > A2
                boolean first = setOneAnswer(answer.getI().getOrderId(), answer.getJ().getOrderId(), 0, 0, true, new ArrayList<>(), quasiExpert, criteriaList, config);
                System.out.println();
                boolean second = setOneAnswer(answer.getJ().getOrderId(), answer.getI().getOrderId(), 0, 0, true, new ArrayList<>(), quasiExpert, criteriaList, config);
                return first || second;
            }
            default: throw new IllegalStateException("Unexpected value: TODO" + answer.getAnswerType());
        }
    }

    /**
     * Данный метод устанавливает ответ пары {@code (i, j)}
     * в данную матрицу. Также устанавливает все вытекающие ответы,
     * исходя из ответа {@code (i, j)}.
     *
     * @param qe
     * @param i один из ответов.
     * @param j другой из ответов.
     * @return {@code true}, если противоречий нет;
     * {@code false}, если противоречия есть (требуется новый квазиэксперт).
     */
    private boolean setOneAnswer(int i, int j, int startWorse, int startBetter, boolean isEqual, List<Point> points,
                                     QuasiExpert qe, List<Criteria> criteriaList, QuasiExpertConfig config) {
        System.out.println("!!! setOneAnswer " +
            ", i: "+getAssessmentByOrderId(i, criteriaList).getName()+"("+i+")"+
            ", j: "+getAssessmentByOrderId(j, criteriaList).getName()+"("+j+")"+
            ", startWorse: "+startWorse+", startBetter: "+startBetter);

        if (!isEqual && qe.getMatrix()[j][i] == 1) return false;

        points.add(Point.of(i, j));
        qe.getMatrix()[i][j] = 1;

        // проверка на оценки хуже
        for (int k = startWorse; k < config.getLen(); k++) {
            if ((k == j) || (config.getIndexes().contains(k))) continue;
            if (getAssessmentByOrderId(i, criteriaList).getCriteriaId() == getAssessmentByOrderId(k, criteriaList).getCriteriaId()) continue;
            if (points.contains(Point.of(i, k))) continue;

            if (qe.getMatrix()[j][k] == 1) {
                setOneAnswer(i, k, k, startBetter, false, points, qe, criteriaList, config);
            }
        }

        // проверка на оценки лучше
        for (int k = startBetter; k < config.getLen(); k++) {
            if ((k == i) || (config.getIndexes().contains(k))) continue;
            if (getAssessmentByOrderId(k, criteriaList).getCriteriaId() == getAssessmentByOrderId(j, criteriaList).getCriteriaId()) continue;
            if (points.contains(Point.of(k, j))) continue;

            if (qe.getMatrix()[k][i] == 1) {
                setOneAnswer(k, j, startWorse, k, false, points, qe, criteriaList, config);
            }
        }

        return true;
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
