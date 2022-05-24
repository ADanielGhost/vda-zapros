package org.polytech.zapros.bean;

import java.util.List;
import java.util.Map;

/**
 * Bean для ранжированной альтернативы.
 */
public class AlternativeResult {

    /**
     * Альтернатива, которой мы присваиваем ранги
     */
    private Alternative alternative;

    /**
     * Ранги оценок квазиэкспертов для данной альтернативы.
     */
    private Map<QuasiExpert, List<Integer>> assessmentsRanks;

    /**
     * Относительные ранги исходя из оценок квазиэкспертов для данной альтернативы.
     */
    private Map<QuasiExpert, Integer> relativeRanks;

    /**
     * Конечный ранг для данной альтернативы.
     */
    private Integer finalRank;

    public Alternative getAlternative() {
        return alternative;
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
    }

    public Map<QuasiExpert, List<Integer>> getAssessmentsRanks() {
        return assessmentsRanks;
    }

    public void setAssessmentsRanks(Map<QuasiExpert, List<Integer>> assessmentsRanks) {
        this.assessmentsRanks = assessmentsRanks;
    }

    public Map<QuasiExpert, Integer> getRelativeRanks() {
        return relativeRanks;
    }

    public void setRelativeRanks(Map<QuasiExpert, Integer> relativeRanks) {
        this.relativeRanks = relativeRanks;
    }

    public Integer getFinalRank() {
        return finalRank;
    }

    public void setFinalRank(Integer finalRank) {
        this.finalRank = finalRank;
    }





    // TODO костыли

    private Map<QuasiExpertQV, Integer> relativeQVRanks;

    public Map<QuasiExpertQV, Integer> getRelativeQVRanks() {
        return relativeQVRanks;
    }

    public void setRelativeQVRanks(Map<QuasiExpertQV, Integer> relativeQVRanks) {
        this.relativeQVRanks = relativeQVRanks;
    }
}
