package org.polytech.zapros.bean;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnswerCheckResult {

    private long id;
    private boolean isOver;
    private List<Answer> answerList;
    private List<Criteria> criteriaList;
    private Integer pCriteriaI;
    private Integer pCriteriaJ;
    private Integer pAssessmentI;
    private Integer pAssessmentJ;

    public AnswerCheckResult(boolean isOver, List<Answer> answerList) {
        this.isOver = isOver;
        this.answerList = answerList;
    }
}
