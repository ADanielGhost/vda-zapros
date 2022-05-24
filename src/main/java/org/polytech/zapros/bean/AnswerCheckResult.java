package org.polytech.zapros.bean;

import java.util.List;

public class AnswerCheckResult {

    private boolean isOver;
    private List<Answer> answerList;
    private List<Criteria> criteriaList;
    private Integer pCriteriaI;
    private Integer pCriteriaJ;
    private Integer pAssessmentI;
    private Integer pAssessmentJ;

    public AnswerCheckResult() {
    }

    public AnswerCheckResult(boolean isOver, List<Answer> answerList) {
        this.isOver = isOver;
        this.answerList = answerList;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public Integer getpCriteriaI() {
        return pCriteriaI;
    }

    public void setpCriteriaI(Integer pCriteriaI) {
        this.pCriteriaI = pCriteriaI;
    }

    public Integer getpCriteriaJ() {
        return pCriteriaJ;
    }

    public void setpCriteriaJ(Integer pCriteriaJ) {
        this.pCriteriaJ = pCriteriaJ;
    }

    public Integer getpAssessmentI() {
        return pAssessmentI;
    }

    public void setpAssessmentI(Integer pAssessmentI) {
        this.pAssessmentI = pAssessmentI;
    }

    public Integer getpAssessmentJ() {
        return pAssessmentJ;
    }

    public void setpAssessmentJ(Integer pAssessmentJ) {
        this.pAssessmentJ = pAssessmentJ;
    }

    @Override
    public String toString() {
        return "AnswerCheck{" +
            "isOver=" + isOver +
            ", pCriteriaI=" + pCriteriaI +
            ", pCriteriaJ=" + pCriteriaJ +
            ", pAssessmentI=" + pAssessmentI +
            ", pAssessmentJ=" + pAssessmentJ +
            '}';
    }
}
