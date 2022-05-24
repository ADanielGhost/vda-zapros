package org.polytech.zapros.bean;

/**
 * Bean для ответа.
 */
public class Answer {

    public enum AnswerType { BETTER, WORSE, EQUAL }

    public enum AnswerAuthor { USER, TRANSIENT, REPLACED }

    private final Assessment i;

    private final Assessment j;

    private final AnswerType answerType;

    private final AnswerAuthor answerAuthor;

    public Answer(Assessment i, Assessment j, AnswerType answerType, AnswerAuthor answerAuthor) {
        this.i = i;
        this.j = j;
        this.answerType = answerType;
        this.answerAuthor = answerAuthor;
    }

    public Assessment getI() {
        return i;
    }

    public Assessment getJ() {
        return j;
    }

    public int getIAsId() {
        return i.getId();
    }

    public int getJAsId() {
        return j.getId();
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public AnswerAuthor getAnswerAuthor() {
        return answerAuthor;
    }

    @Override
    public String toString() {
        return "Answer{" +
            "i=" + i.getName() + ":" + i.getId() +
            ", j=" + j.getName() + ":" + j.getId() +
            ", answerType=" + answerType +
            ", answerAuthor=" + answerAuthor +
            '}';
    }
}
