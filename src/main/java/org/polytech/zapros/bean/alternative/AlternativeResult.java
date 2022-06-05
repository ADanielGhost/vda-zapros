package org.polytech.zapros.bean.alternative;

import org.polytech.zapros.bean.Alternative;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для ранжированной альтернативы.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class AlternativeResult {

    private long id;

    /**
     * Альтернатива, которой мы присваиваем ранги
     */
    private Alternative alternative;

    /**
     * Конечный ранг для данной альтернативы.
     */
    private Integer finalRank;
}
