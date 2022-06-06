package org.polytech.zapros.service.qe;

import org.polytech.zapros.service.buildqe.BuildQesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.polytech.zapros.service.correcter.CorrectingContradictionsService;
import org.polytech.zapros.service.validating.ValidatingQesService;

@Component
public class QuasiExpertAraceService extends QuasiExpertServiceImpl {

    @Autowired
    public QuasiExpertAraceService(
            BuildQesService buildQesService,
            @Qualifier("validatingQesAraceService") ValidatingQesService validatingQesService,
            CorrectingContradictionsService correctingContradictionsService) {
        super(buildQesService, validatingQesService, correctingContradictionsService);
    }
}
