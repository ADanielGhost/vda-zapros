package org.polytech.zapros.bean;

import java.util.List;

/**
 * Bean для критерия.
 */
public class Criteria {

    /**
     * Имя для данного критерия.
     */
    private final String name;

    /**
     * Уникальный номер.
     */
    private final int id;

    /**
     * Кол-во оценок.
     */
    private final int numOfAssessments;

    /**
     * Сами оценки.
     */
    private final List<Assessment> assessments;

    public Criteria(String name, int id, List<Assessment> assessments) {
        this.name = name;
        this.id = id;
        this.numOfAssessments = assessments.size();
        this.assessments = assessments;
    }

    //region getters
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getNumOfAssessments() {
        return this.numOfAssessments;
    }

    public List<Assessment> getAssessments() {
        return this.assessments;
    }
    //endregion
}
