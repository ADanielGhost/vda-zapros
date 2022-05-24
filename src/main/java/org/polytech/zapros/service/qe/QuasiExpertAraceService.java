package org.polytech.zapros.service.qe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.polytech.zapros.service.correcter.CorrectingContradictionsService;
import org.polytech.zapros.service.validating.ValidatingQesService;

@Component
public class QuasiExpertAraceService extends QuasiExpertServiceImpl {

    @Autowired
    public QuasiExpertAraceService(
            @Qualifier("validatingQesAraceService") ValidatingQesService validatingQesService,
            CorrectingContradictionsService correctingContradictionsService) {
        super(validatingQesService, correctingContradictionsService);
    }
}
