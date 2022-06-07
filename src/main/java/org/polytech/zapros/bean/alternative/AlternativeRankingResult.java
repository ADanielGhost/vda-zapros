package org.polytech.zapros.bean.alternative;

import java.util.List;
import java.util.Map;

import org.polytech.zapros.bean.QuasiExpert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeRankingResult {
    private List<? extends AlternativeResult> alternativeResults;
    private long timeMS;
    private Map<QuasiExpert, Map<AlternativePair, CompareType>> mapCompare;
}
