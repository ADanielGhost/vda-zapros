package org.polytech.zapros.service.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.polytech.zapros.service.answer.AnswerService;
import org.polytech.zapros.service.qe.QuasiExpertService;
import org.polytech.zapros.service.ranking.AlternativeRankingService;

@Component
public class AraceQVService extends VdaZaprosServiceImpl {

    @Autowired
    public AraceQVService(
            AnswerService answerService,
            @Qualifier("quasiExpertAraceService") QuasiExpertService quasiExpertService,
            @Qualifier("alternativeQVRankingService") AlternativeRankingService alternativeRankingService) {
        super(answerService, quasiExpertService, alternativeRankingService);
    }
}
