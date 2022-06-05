package org.polytech.zapros.bean;

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
}
