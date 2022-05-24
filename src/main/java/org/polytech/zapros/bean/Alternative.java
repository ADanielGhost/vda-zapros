package org.polytech.zapros.bean;

import java.util.List;

/**
 * Bean для альтернативы.
 */
public class Alternative {

    /**
     * Имя данной альтернативы.
     */
    private final String name;

    /**
     * Оценки данной альтернативы.
     */
    private final List<Assessment> assessments;

    public Alternative(String name, List<Assessment> assessments) {
        this.name = name;
        this.assessments = assessments;
    }

    public String getName() {
        return this.name;
    }

    public List<Assessment> getAssessments() {
        return this.assessments;
    }
}
