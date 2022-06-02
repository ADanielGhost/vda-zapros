package org.polytech.zapros.service.validating;

import java.util.List;

import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;

@Component
public class ValidatingQesZaprosService implements ValidatingQesService {

    @Override
    public boolean isQesValid(List<QuasiExpert> qes, QuasiExpertConfig config, Double threshold) {
        return qes.size() == 1;
    }
}
