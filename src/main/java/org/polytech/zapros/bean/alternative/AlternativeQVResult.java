package org.polytech.zapros.bean.alternative;

import java.util.Map;

import org.polytech.zapros.bean.QuasiExpertQV;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlternativeQVResult extends AlternativeResult {
    private Map<QuasiExpertQV, Integer> relativeQVRanks;

    @Override
    public String toString() {
        return "AlternativeQVResult{" +
            "id=" + getId() +
            ", alternative=" + getAlternative() +
            ", finalRank=" + getFinalRank() +
            ", relativeQVRanks=" + relativeQVRanks +
            '}';
    }
}
