package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Bean для критерия.
 */
@Getter
@AllArgsConstructor
@ToString
public class Criteria {

    /**
     * Уникальный номер.
     */
    private final long id;

    /**
     * Имя для данного критерия.
     */
    private final String name;

    /**
     * Сами оценки.
     */
    private final List<Assessment> assessments;

    private final int orderId;
}
