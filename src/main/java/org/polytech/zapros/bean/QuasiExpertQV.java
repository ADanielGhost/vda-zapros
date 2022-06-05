package org.polytech.zapros.bean;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuasiExpertQV {
    private QuasiExpert quasiExpert;
    private Map<QualityVariation, Integer> qualityVariationMap;
}
