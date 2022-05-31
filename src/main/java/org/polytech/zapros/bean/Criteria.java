package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для критерия.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Criteria {

    /**
     * Уникальный номер.
     */
    private long id;

    /**
     * Имя для данного критерия.
     */
    private String name;

    /**
     * Сами оценки.
     */
    private List<Assessment> assessments;

    private int orderId;
}
