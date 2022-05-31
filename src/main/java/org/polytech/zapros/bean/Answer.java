package org.polytech.zapros.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Bean для ответа.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Answer {

    public enum AnswerType { BETTER, WORSE, EQUAL }

    public enum AnswerAuthor { USER, TRANSIENT, REPLACED }

    private long id;
    private final Assessment i;
    private final Assessment j;
    private final AnswerType answerType;
    private final AnswerAuthor answerAuthor;

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
