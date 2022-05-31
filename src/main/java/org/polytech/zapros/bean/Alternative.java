package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Bean для альтернативы.
 */
@Getter
@AllArgsConstructor
@ToString
public class Alternative {

    private final long id;

    /**
     * Имя данной альтернативы.
     */
    private final String name;

    /**
     * Оценки данной альтернативы.
     */
    private final List<Assessment> assessments;
}
