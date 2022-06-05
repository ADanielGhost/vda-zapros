package org.polytech.zapros.bean.alternative;

import java.util.Map;

import org.polytech.zapros.bean.QuasiExpertQV;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AlternativeQVResult extends AlternativeResult {
    private Map<QuasiExpertQV, Integer> relativeQVRanks;
}
