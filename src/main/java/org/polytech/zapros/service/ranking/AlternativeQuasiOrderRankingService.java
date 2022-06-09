package org.polytech.zapros.service.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polytech.zapros.bean.alternative.AlternativePair;
import org.polytech.zapros.bean.alternative.AlternativeRankingResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.alternative.AlternativeOrderResult;
import org.polytech.zapros.bean.alternative.CompareType;
import org.polytech.zapros.comparator.MyComparator;
import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.comparator.AlternativeFinalRankOrderComparator;
import org.polytech.zapros.comparator.AlternativeRelativeRanksOrderComparator;

@Component
public class AlternativeQuasiOrderRankingService implements AlternativeRankingService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public AlternativeRankingResult rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config) {
        log.info("rankAlternatives started");
        long timeStart = System.nanoTime();
//        System.out.println("Ранги оценок для альтернативы:");
        List<AlternativeOrderResult> result = alternativeList.stream()
            .map(alternative -> {
                AlternativeOrderResult alternativeResult = new AlternativeOrderResult();
                alternativeResult.setAlternative(alternative);
                alternativeResult.setRelativeRanks(new HashMap<>());
                alternativeResult.setAssessmentsRanks(calcAssessmentsRanks(alternative, qes));
                return alternativeResult;
            })
            .collect(Collectors.toList());
        System.out.println();

        Map<QuasiExpert, Map<AlternativePair, CompareType>> mapCompare = new HashMap<>();
        for (QuasiExpert qe: qes) {
            Map<AlternativePair, CompareType> tempMap = new HashMap<>();
            setRelativeRanks(result, qe, tempMap);
            mapCompare.put(qe, tempMap);
        }
        setFinalRank(result);

        long timeEnd = System.nanoTime();
        log.info("rankAlternatives finished");
        return new AlternativeRankingResult(result, timeEnd - timeStart, mapCompare);
    }

    private void setRelativeRanks(List<AlternativeOrderResult> alternativeResultList, QuasiExpert qe, Map<AlternativePair, CompareType> mapCompare) {
        MyComparator<AlternativeOrderResult> comparator = new AlternativeRelativeRanksOrderComparator(qe);

        Map<AlternativeOrderResult, Integer> map = new HashMap<>();
        for (AlternativeOrderResult alternativeResult: alternativeResultList) {
            map.put(alternativeResult, 0);
        }

//        System.out.println("!!!!!! compareOrder !!!!!!!");
        for (int i = 0; i < alternativeResultList.size(); i++) {
            for (int j = i; j < alternativeResultList.size(); j++) {
                if (i == j) continue;

                AlternativeOrderResult alternativeI = alternativeResultList.get(i);
                AlternativeOrderResult alternativeJ = alternativeResultList.get(j);

                CompareType compare = comparator.compareWithType(alternativeI, alternativeJ);
//                System.out.println("!!! so type is " + compare);
//                System.out.println();
                mapCompare.put(AlternativePair.of(alternativeI.getAlternative(), alternativeJ.getAlternative()), compare);

                switch (compare) {
                    case BETTER: {
                        int value = map.get(alternativeJ);
                        value++;
                        map.put(alternativeJ, value);
                        break;
                    }
                    case WORSE: {
                        int value = map.get(alternativeI);
                        value++;
                        map.put(alternativeI, value);
                        break;
                    }
                    case EQUAL: case NOT_COMPARABLE: {
                        int valueI = map.get(alternativeI);
                        int valueJ = map.get(alternativeJ);
                        valueI++;
                        valueJ++;
                        map.put(alternativeI, valueI);
                        map.put(alternativeJ, valueJ);
                        break;
                    }
                    default: throw new IllegalArgumentException("compare in altRanking type");
                }
            }
        }

        List<Map.Entry<AlternativeOrderResult, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort((e1, e2) -> {
            Integer v1 = e1.getValue();
            Integer v2 = e2.getValue();
            return v1.compareTo(v2);
        });

        int cur = 1;
        for (int i = 0; i < entries.size() - 1; i++) {
            int oldValue = entries.get(i).getValue();
            entries.get(i).setValue(cur);
            if (entries.get(i + 1).getValue().compareTo(oldValue) != 0) {
                cur++;
            }
        }
        entries.get(entries.size() - 1).setValue(cur);

        // TODO SIDE-EFFECTS!
//        System.out.println("Entries RANKS");
//        entries.stream().map(entry -> (entry.getKey().getAlternative().getName() + " " + entry.getValue())).forEach(System.out::println);
//        System.out.println();

        for (AlternativeOrderResult alternativeResult: alternativeResultList) {
            alternativeResult.getRelativeRanks().put(qe, map.get(alternativeResult));
        }
    }

    private void setFinalRank(List<AlternativeOrderResult> alternativeResultList) {
        int cur = 1;
        Comparator<AlternativeOrderResult> comparator = new AlternativeFinalRankOrderComparator();
        alternativeResultList.sort(comparator);

        for (int i = 0; i < alternativeResultList.size() - 1; i++) {
            alternativeResultList.get(i).setFinalRank(cur);

            if (comparator.compare(alternativeResultList.get(i), alternativeResultList.get(i + 1)) != 0) {
                cur++;
            }
        }

        alternativeResultList.get(alternativeResultList.size() - 1).setFinalRank(cur);
    }

    private Map<QuasiExpert, List<Integer>> calcAssessmentsRanks(Alternative alternative, List<QuasiExpert> qes) {
        Map<QuasiExpert, List<Integer>> result = new HashMap<>();

        for (QuasiExpert quasiExpert: qes) {
            List<Integer> localRanks = new ArrayList<>();
            for (Assessment assessment: quasiExpert.getRanks().keySet()) {
                if (alternative.getAssessments().contains(assessment)) {
                    localRanks.add(quasiExpert.getRanks().get(assessment));
                }
            }

            localRanks.sort(Comparator.naturalOrder());
            result.put(quasiExpert, localRanks);
        }

//        System.out.println(alternative.getName());
//        result.forEach((k, v) -> System.out.println("qe" + k.getId() + " " + v.stream().map(String::valueOf).collect(Collectors.joining(" "))));
        return result;
    }
}
