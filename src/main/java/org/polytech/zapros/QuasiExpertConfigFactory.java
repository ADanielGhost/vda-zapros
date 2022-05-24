package org.polytech.zapros;

import java.util.List;

import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpertConfig;

public class QuasiExpertConfigFactory {
    public static QuasiExpertConfig getConfig(List<Criteria> criteriaList, Double threshold) {
        return new QuasiExpertConfig(criteriaList, threshold);
    }
}
