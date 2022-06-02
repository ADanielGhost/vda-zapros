package org.polytech.zapros.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Bean для ответа.
 */
@Getter
@Setter
@NoArgsConstructor
public class Answer {

    public enum AnswerType { BETTER, WORSE, EQUAL }

    public enum AnswerAuthor { USER, TRANSIENT, REPLACED }

    private long id;
    private Assessment i;
    private Assessment j;
    private AnswerType answerType;
    private AnswerAuthor answerAuthor;

    public Answer(Assessment i, Assessment j, AnswerType answerType, AnswerAuthor answerAuthor) {
        this.i = i;
        this.j = j;
        this.answerType = answerType;
        this.answerAuthor = answerAuthor;
    }

    public int getIAsId() {
        return i.getOrderId();
    }
    public int getJAsId() {
        return j.getOrderId();
    }

    @Override
    public String toString() {
        return "Answer{" +
            "i=" + i.getName() + ":" + i.getOrderId() +
            ", j=" + j.getName() + ":" + j.getOrderId() +
            ", answerType=" + answerType +
            ", answerAuthor=" + answerAuthor +
            '}';
    }
}
