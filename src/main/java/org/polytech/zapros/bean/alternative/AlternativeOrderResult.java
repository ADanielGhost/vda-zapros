package org.polytech.zapros.bean.alternative;

import java.util.List;
import java.util.Map;

import org.polytech.zapros.bean.QuasiExpert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class AlternativeOrderResult extends AlternativeResult {
    /**
     * Ранги оценок квазиэкспертов для данной альтернативы.
     */
    private Map<QuasiExpert, List<Integer>> assessmentsRanks;

    /**
     * Относительные ранги исходя из оценок квазиэкспертов для данной альтернативы.
     */
    private Map<QuasiExpert, Integer> relativeRanks;

    @Override
    public String toString() {
        return "AlternativeOrderResult{" +
            "id=" + getId() +
            ", alternative=" + getAlternative() +
            ", finalRank=" + getFinalRank() +
            ", assessmentsRanks=" + assessmentsRanks +
            ", relativeRanks=" + relativeRanks +
            '}';
    }
}
