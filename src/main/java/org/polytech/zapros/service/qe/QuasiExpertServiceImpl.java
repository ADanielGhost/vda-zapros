package org.polytech.zapros.service.qe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.service.buildqe.BuildQesService;
import org.polytech.zapros.service.correcter.CorrectingContradictionsService;
import org.polytech.zapros.service.validating.ValidatingQesService;

public abstract class QuasiExpertServiceImpl implements QuasiExpertService {

    private final Log log = LogFactory.getLog(this.getClass());

    private final BuildQesService buildQesService;
    private final ValidatingQesService validatingQesService;
    private final CorrectingContradictionsService correctingContradictionsService;

    public QuasiExpertServiceImpl(BuildQesService buildQesService, ValidatingQesService validatingQesService, CorrectingContradictionsService correctingContradictionsService) {
        this.buildQesService = buildQesService;
        this.validatingQesService = validatingQesService;
        this.correctingContradictionsService = correctingContradictionsService;
    }

    @Override
    public BuildingQesCheckResult buildQes(List<Answer> answerList, QuasiExpertConfig config, List<Criteria> criteriaList, Double threshold) {
        log.info("buildQes started");
        List<QuasiExpert> result = buildQesService.build(answerList, criteriaList, config);

        if (!validatingQesService.isQesValid(result, config, threshold)) {
            return correctingContradictionsService.correct(answerList, result, config, criteriaList);
        }

        result.forEach(x -> calculateRang(x, criteriaList, config));

        log.info("buildQes ended with qe: " + result.size());
        return new BuildingQesCheckResult(true, result);
    }

    /**
     * Данный метод присваивает ранги оценкам.
     */
    public void calculateRang(QuasiExpert quasiExpert, List<Criteria> criteriaList, QuasiExpertConfig config) {
        int cur = 0;
        Map<Assessment, Integer> ranks = new HashMap<>();

        for (Criteria criteria: criteriaList) {
            for (Assessment assessment: criteria.getAssessments()) {
                int rank = 1;
                for (int i = 0; i < config.getLen(); i++) {
                    if (quasiExpert.getMatrix()[cur][i] != 1) {
                        rank++;
                    }
                }
                cur++;

                ranks.put(assessment, rank);
            }
        }
        quasiExpert.setRanks(ranks);
    }
}
