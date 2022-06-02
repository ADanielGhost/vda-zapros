package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для оценки квазиэксперта.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Assessment {

    private long id;

    /**
     * Имя для данной оценки.
     */
    private String name;

    /**
     * Id критерия, которому она принадлежит.
     */
    private long criteriaId;

    /**
     * Ранг среди оценок одного критерия.
     */
    private int rank;

    /**
     * Уникальный номер.
     */
    private int orderId;

    public static Assessment getByOrderId(int orderId, List<Criteria> criteriaList) {
        for (Criteria c: criteriaList) {
            for (Assessment a: c.getAssessments()) {
                if (orderId == a.getOrderId()) return a;
            }
        }
        // невозможный кейс
        throw new IllegalStateException("Assessment.getByOrderId failed");
    }
}
