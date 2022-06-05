package org.polytech.zapros.bean;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@ToString
public class AlternativeResult {

    private long id;

    /**
     * Альтернатива, которой мы присваиваем ранги
     */
    private Alternative alternative;

    /**
     * Ранги оценок квазиэкспертов для данной альтернативы.
     */
    private Map<QuasiExpert, List<Integer>> assessmentsRanks;

    /**
     * Относительные ранги исходя из оценок квазиэкспертов для данной альтернативы.
     */
    private Map<QuasiExpert, Integer> relativeRanks;

    /**
     * Конечный ранг для данной альтернативы.
     */
    private Integer finalRank;

    // TODO костыли
    private Map<QuasiExpertQV, Integer> relativeQVRanks;
}
