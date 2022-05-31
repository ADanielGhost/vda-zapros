package org.polytech.zapros.bean;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для критерия.
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Criteria {

    /**
     * Уникальный номер.
     */
    private int id;

    /**
     * Имя для данного критерия.
     */
    private final String name;

    /**
     * Сами оценки.
     */
    private final List<Assessment> assessments;
}
