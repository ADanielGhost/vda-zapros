package org.polytech.zapros.service.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Criteria;
import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.AlternativeResult;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.comparator.AlternativeFinalRankComparator;
import org.polytech.zapros.comparator.AlternativeRelativeRanksComparator;

@Component
public class AlternativeQuasiOrderRankingService implements AlternativeRankingService {

    public List<AlternativeResult> rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config) {

        List<AlternativeResult> result = alternativeList.stream()
            .map(alternative -> {
                AlternativeResult alternativeResult = new AlternativeResult();
                alternativeResult.setAlternative(alternative);
                alternativeResult.setRelativeRanks(new HashMap<>());
                // TODO ORDERED RANKS? ARACE?
                alternativeResult.setAssessmentsRanks(getQeOrderedRanks(alternative, qes));
                return alternativeResult;
            })
            .collect(Collectors.toList());

        for (QuasiExpert qe: qes) {
            setRelativeRanks(result, qe);
        }
        setFinalRank(result);

        return result;
    }

    private void setRelativeRanks(List<AlternativeResult> alternativeResultList, QuasiExpert quasiExpert) {
        int cur = 1;

        Comparator<AlternativeResult> comparator = new AlternativeRelativeRanksComparator(quasiExpert);
        alternativeResultList.sort(comparator);

        for (int i = 0; i < alternativeResultList.size() - 1; i++) {
            alternativeResultList.get(i).getRelativeRanks().put(quasiExpert, cur);

            if (comparator.compare(alternativeResultList.get(i), alternativeResultList.get(i + 1)) != 0) {
                cur++;
            }
        }

        alternativeResultList.get(alternativeResultList.size() - 1).getRelativeRanks().put(quasiExpert, cur);
    }

    private void setFinalRank(List<AlternativeResult> alternativeResultList) {
        int cur = 1;
        Comparator<AlternativeResult> comparator = new AlternativeFinalRankComparator();
        alternativeResultList.sort(comparator);

        for (int i = 0; i < alternativeResultList.size() - 1; i++) {
            alternativeResultList.get(i).setFinalRank(cur);

            if (comparator.compare(alternativeResultList.get(i), alternativeResultList.get(i + 1)) != 0) {
                cur++;
            }
        }

        alternativeResultList.get(alternativeResultList.size() - 1).setFinalRank(cur);
    }

    private Map<QuasiExpert, List<Integer>> getQeOrderedRanks(Alternative alternative, List<QuasiExpert> qes) {
        Map<QuasiExpert, List<Integer>> result = new HashMap<>();

        for (QuasiExpert quasiExpert: qes) {
            List<Integer> localRanks = new ArrayList<>();
            for (Assessment assessment: quasiExpert.getOrderedRanks().keySet()) {
                if (alternative.getAssessments().contains(assessment)) {
                    localRanks.add(quasiExpert.getOrderedRanks().get(assessment));
                }
            }

            localRanks.sort(Comparator.naturalOrder());
            result.put(quasiExpert, localRanks);
        }

        return result;
    }
}
