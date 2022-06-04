package org.polytech.zapros.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Bean для квазиэксперта.
 */
public class QuasiExpert {

    /**
     * Матрица данного квазиэксперта.
     */
    private int[][] matrix;

    private Answer firstAnswer;

    /**
     * Ранги, которые данный квазиэксперт присвоил оценкам.
     */
    private Map<Assessment, Integer> ranks;

    private Map<Assessment, Integer> orderedRanks;
    /**
     * Конфиг для квазиэксперта.
     */
    private QuasiExpertConfig quasiExpertConfig;

    public QuasiExpert(QuasiExpertConfig quasiExpertConfig) {
        this.quasiExpertConfig = quasiExpertConfig;
        this.matrix = initializationThisData();
    }

    /**
     * Данный метод копирует матрицу из конфига.
     * @return склонированная матрица.
     */
    private int[][] initializationThisData() {
        int[][] result = new int[quasiExpertConfig.getLen()][quasiExpertConfig.getLen()];
        for (int i = 0; i < quasiExpertConfig.getLen(); i++) {
            System.arraycopy(
                quasiExpertConfig.getInitData()[i], 0,
                result[i], 0,
                quasiExpertConfig.getLen()
            );
        }

        return result;
    }


    public boolean setOneQe(Answer answer) {
        switch (answer.getAnswerType()) {
            case BETTER: return setOneQe(answer.getIAsId(), answer.getJAsId());
            case WORSE: return setOneQe(answer.getJAsId(), answer.getIAsId());
            case EQUAL: throw new NotImplementedException();
            default: throw new IllegalStateException("Unexpected value: TODO" + answer.getAnswerType());
        }
    }

    /**
     * Данный метод устанавливает ответ пары {@code (i, j)}
     * в данную матрицу. Также устанавливает все вытекающие ответы,
     * исходя из ответа {@code (i, j)}.
     * @param i один из ответов.
     * @param j другой из ответов.
     * @return {@code true}, если противоречий нет;
     * {@code false}, если противоречия есть (требуется новый квазиэксперт).
     */
    private boolean setOneQe(int i, int j) {
        if (this.matrix[j][i] == 1) return false;

        this.matrix[i][j] = 1;

        // проверка на оценки хуже
        for (int k = 0; k < quasiExpertConfig.getLen(); k++) {
            if ((k == j) || (quasiExpertConfig.getIndexes().contains(k))) continue;

            if (this.matrix[j][k] == 1) {
                setOneQe(i, k);
            }
        }

        // проверка на оценки лучше
        for (int k = 0; k < quasiExpertConfig.getLen(); k++) {
            if ((k == i) || (quasiExpertConfig.getIndexes().contains(k))) continue;

            if (this.matrix[k][i] == 1) {
                setOneQe(k, j);
            }
        }

        return true;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public QuasiExpert() {
    }

    public void setRanks(Map<Assessment, Integer> ranks) {
        this.ranks = ranks;
    }

    public Answer getFirstAnswer() {
        return firstAnswer;
    }

    public void setFirstAnswer(Answer firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    public Map<Assessment, Integer> getRanks() {
        return this.ranks;
    }

    public Map<Assessment, Integer> getOrderedRanks() {
        return orderedRanks;
    }

    // TODO service
    /**
     * Данный метод присваивает ранги оценкам.
     */
    public void calculateRang(List<Criteria> criteriaList) {
        int cur = 0;
        ranks = new HashMap<>();

        for (Criteria c: criteriaList) {
            for (Assessment a: c.getAssessments()) {
                int rank = 1;
                for (int i = 0; i < quasiExpertConfig.getLen(); i++) {
                    if (this.matrix[cur][i] != 1) {
                        rank++;
                    }
                }
                cur++;

                ranks.put(a, rank);
            }
        }

        orderRank();
    }

    private void orderRank() {
        orderedRanks = new HashMap<>();
        for (Assessment key: ranks.keySet()) {
            orderedRanks.put(key, ranks.get(key));
        }

        List<Map.Entry<Assessment, Integer>> entries = new ArrayList<>(orderedRanks.entrySet());
        entries.sort((e1, e2) -> {
            Integer v1 = e1.getValue();
            Integer v2 = e2.getValue();
            return v1.compareTo(v2);
        });

        int cur = 0;
        for (Map.Entry<Assessment, Integer> entry: entries) {
            if (entry.getValue() > cur && entry.getValue() != 1) {
                cur++;
            }
            entry.setValue(cur);
        }
    }
}
