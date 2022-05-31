package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для альтернативы.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Alternative {

    private long id;

    /**
     * Имя данной альтернативы.
     */
    private String name;

    /**
     * Оценки данной альтернативы.
     */
    private List<Assessment> assessments;
}
