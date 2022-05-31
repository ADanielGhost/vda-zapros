package org.polytech.zapros.bean;

import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Bean для оценки квазиэксперта.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Assessment {

    private long id;

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
    private int orderId;

    public static void calculateId(List<Criteria> criteriaList) {
        criteriaList.forEach(criteria -> criteria.getAssessments().sort(Comparator.comparingInt(Assessment::getRank)));
        int cur = 0;
        for (Criteria c: criteriaList) {
            for (Assessment a: c.getAssessments()) {
                a.orderId = cur;
                cur++;
            }
        }
    }

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
