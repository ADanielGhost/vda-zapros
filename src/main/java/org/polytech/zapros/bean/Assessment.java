package org.polytech.zapros.bean;

import java.util.List;

/**
 * Bean для оценки квазиэксперта.
 */
public class Assessment {

    /**
     * Имя для данной оценки.
     */
    private final String name;

    /**
     * Id критерия, которому она принадлежит.
     */
    private final int criteriaId;

    /**
     * Ранг среди оценок одного критерия.
     */
    private final int rank;

    /**
     * Уникальный номер.
     */
    private int id;

    public Assessment(String name, int criteriaId, int rang){
        this.name = name;
        this.criteriaId = criteriaId;
        this.rank = rang;
    }

    public static void calculateId(List<Criteria> criteria) {
        int cur = 0;
        for (Criteria c: criteria) {
            for (Assessment a: c.getAssessments()) {
                a.id = cur;
                cur++;
            }
        }
    }

    public static Assessment getById(int id, List<Criteria> criteriaList) {
        for (Criteria c: criteriaList) {
            for (Assessment a: c.getAssessments()) {
                if (id == a.getId()) return a;
            }
        }
        // невозможный кейс
        throw new IllegalStateException("Assessment.getById failed");
    }

    //region getters
    public String getName() {
        return this.name;
    }

    public int getCriteriaId() {
        return this.criteriaId;
    }

    public int getRank() {
        return this.rank;
    }

    public int getId() {
        return this.id;
    }
    //endregion

    @Override
    public String toString() {
        return "Assessment{" +
            "name='" + name + '\'' +
            ", criteriaId=" + criteriaId +
            ", rank=" + rank +
            ", id=" + id +
            '}';
    }
}
