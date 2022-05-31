package org.polytech.zapros.bean;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для альтернативы.
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Alternative {

    private long id;

    /**
     * Имя данной альтернативы.
     */
    private final String name;

    /**
     * Оценки данной альтернативы.
     */
    private final List<Assessment> assessments;
}
