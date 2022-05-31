package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Bean для оценки квазиэксперта.
 */
@Getter
@AllArgsConstructor
public class Assessment {

    private final long id;

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
    private final int orderId;

    @Deprecated
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
