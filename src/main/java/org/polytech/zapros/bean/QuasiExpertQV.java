package org.polytech.zapros.bean;

import java.util.Map;

public class QuasiExpertQV {
    private QuasiExpert quasiExpert;
    private Map<QualityVariation, Integer> qualityVariationMap;

    public QuasiExpert getQuasiExpert() {
        return quasiExpert;
    }

    public void setQuasiExpert(QuasiExpert quasiExpert) {
        this.quasiExpert = quasiExpert;
    }

    public Map<QualityVariation, Integer> getQualityVariationMap() {
        return qualityVariationMap;
    }

    public void setQualityVariationMap(Map<QualityVariation, Integer> qualityVariationMap) {
        this.qualityVariationMap = qualityVariationMap;
    }
}
