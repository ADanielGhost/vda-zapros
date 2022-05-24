package org.polytech.zapros.bean;

import java.util.List;

public class BuildingQesCheckResult {

    private boolean isOver;
    private List<QuasiExpert> qes;
    private Answer answerForReplacing;
    private List<Answer> answerList;

    public BuildingQesCheckResult() {
    }

    public BuildingQesCheckResult(boolean isOver, List<QuasiExpert> qes) {
        this.isOver = isOver;
        this.qes = qes;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public List<QuasiExpert> getQes() {
        return qes;
    }

    public void setQes(List<QuasiExpert> qes) {
        this.qes = qes;
    }

    public Answer getAnswerForReplacing() {
        return answerForReplacing;
    }

    public void setAnswerForReplacing(Answer answerForReplacing) {
        this.answerForReplacing = answerForReplacing;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
