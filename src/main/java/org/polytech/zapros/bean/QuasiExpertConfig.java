package org.polytech.zapros.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Конфиг для всех квазиэкспертов.
 * <p>
 * Содержит общие данные для всех квазиэкспертов.
 */
public class QuasiExpertConfig {

    /**
     * Список критериев.
     */
    private final List<Criteria> criteriaList;

    /**
     * Длина матрицы квазиэксперта.
     * По сути - сумма количества оценок всех критерий.
     * <p>
     * К примеру, для 3-ех критериев с 3-мя оценками каждый,
     * {@code len = 9}.
     */
    private final int len;

    /**
     * Порог для противоречий квазиэкспертов
     */
    private final double threshold;

    /**
     * Вспомогательная структура, хранящая границы оценок для квазиэксперта
     * <p> К примеру, для 3-ех критериев с 3-мя оценками каждый,
     * {@code indexes = {3, 6, 9}}.
     */
    private final List<Integer> indexes;

    /**
     * Начальная матрица для всех квазиэкспертов, содержащая оценки по умолчанию
     * (т. е. A<sub>1</sub> > A<sub>2</sub> > A<sub>3</sub>, B<sub>1</sub> > B<sub>2</sub> > ...).
     */
    private final int[][] initData;

    public QuasiExpertConfig(List<Criteria> criteriaList, double threshold) {
        this.criteriaList = criteriaList;
        this.len = calculateLen();
        this.threshold = threshold;
        this.indexes = calculateIndexes();
        this.initData = calculateInitData();
    }

    //region calculateDataForInit
    private int calculateLen() {
        return criteriaList
            .stream()
            .map(Criteria::getNumOfAssessments)
            .reduce(0, Integer::sum);
    }

    private List<Integer> calculateIndexes() {
        List<Integer> result = new ArrayList<>();

        int count = 0;
        for (Criteria criteria: criteriaList) {
            result.add(count);
            count += criteria.getNumOfAssessments();
        }

        return result;
    }

    private int[][] calculateInitData() {
        int[][] result = new int[len][len];

        int cur = 0;
        for (Criteria criteria: criteriaList) {
            for (int i = 0; i < len; i++) {
                result[cur][i] = 1;
            }

            int border = cur + criteria.getNumOfAssessments();
            for (int i = cur + 1; i < border; i++) {
                for (int j = cur + 1; j < border; j++) {
                    if (i <= j) result[i][j] = 1;
                }
            }

            cur = border;
        }

        return result;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public int getLen() {
        return len;
    }

    public double getThreshold() {
        return threshold;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public int[][] getInitData() {
        return initData;
    }
}
