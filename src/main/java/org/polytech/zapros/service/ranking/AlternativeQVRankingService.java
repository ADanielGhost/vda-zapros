package org.polytech.zapros.service.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.alternative.AlternativePair;
import org.polytech.zapros.bean.alternative.AlternativeRankingResult;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QualityVariation;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.bean.QuasiExpertQV;
import org.polytech.zapros.bean.alternative.AlternativeQVResult;
import org.polytech.zapros.bean.alternative.CompareType;
import org.polytech.zapros.comparator.AlternativeFinalRankQVComparator;
import org.polytech.zapros.comparator.AlternativeRelativeRanksQVComparator;
import org.polytech.zapros.comparator.MyComparator;
import org.springframework.stereotype.Component;

@Component
public class AlternativeQVRankingService implements AlternativeRankingService {

    private final Log log = LogFactory.getLog(this.getClass());

    private List<QuasiExpertQV> getRanksBLUE(List<QuasiExpert> qes, List<Criteria> criteriaList) {
        System.out.println("Оценки квазиэкспертов:");
        qes.forEach(x -> {
            x.getRanks().forEach((k, v) -> System.out.println(k.getName() + " " + v));
            System.out.println();
        });
        System.out.println();

        List<QuasiExpertQV> result = new ArrayList<>();

        for (QuasiExpert quasiExpert: qes) {
            Map<QualityVariation, Integer> map = new HashMap();

            for (Criteria criteria: criteriaList) {
                List<Assessment> list = criteria.getAssessments();

                for (int i = 0; i < list.size(); i++) {
                    for (int j = i; j < list.size(); j++) {
                        if (i == j) continue;

                        QualityVariation qualityVariation = new QualityVariation(list.get(i), list.get(j));
                        map.put(qualityVariation, quasiExpert.getRanks().get(list.get(j)) - quasiExpert.getRanks().get(list.get(i)));
                    }
                }
            }

            QuasiExpertQV qv = new QuasiExpertQV();
            qv.setQuasiExpert(quasiExpert);
            qv.setQualityVariationMap(map);
            result.add(qv);
        }

        System.out.println("Изменения качества квазиэкспертов:");
        result.forEach(x -> {
            x.getQualityVariationMap().forEach((k, v) -> System.out.println(k.getI().getName() + "->" + k.getJ().getName() + " " + v));
            System.out.println();
        });
        System.out.println();
        return result;
    }

    @Override
    public AlternativeRankingResult rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config) {
        log.info("rankAlternatives started");
        long timeStart = System.nanoTime();
        List<QuasiExpertQV> qeqvs = getRanksBLUE(qes, criteriaList);

        List<AlternativeQVResult> result = alternativeList.stream()
            .map(alternative -> {
                AlternativeQVResult alternativeResult = new AlternativeQVResult();
                alternativeResult.setAlternative(alternative);
                alternativeResult.setRelativeQVRanks(new HashMap<>());
                return alternativeResult;
            })
            .collect(Collectors.toList());

        Map<QuasiExpert, Map<AlternativePair, CompareType>> mapCompare = new HashMap<>();
        for (QuasiExpertQV qe: qeqvs) {
            Map<AlternativePair, CompareType> tempMap = new HashMap<>();
            setRelativeRanks(result, qe, tempMap);
            mapCompare.put(qe.getQuasiExpert(), tempMap);
        }

        setFinalRank(result);

        long timeEnd = System.nanoTime();
        log.info("rankAlternatives finished");
        return new AlternativeRankingResult(result, timeEnd - timeStart, mapCompare);
    }

    private void setFinalRank(List<AlternativeQVResult> alternativeResultList) {
        int cur = 1;
        Comparator<AlternativeQVResult> comparator = new AlternativeFinalRankQVComparator();
        alternativeResultList.sort(comparator);

        for (int i = 0; i < alternativeResultList.size() - 1; i++) {
            alternativeResultList.get(i).setFinalRank(cur);

            if (comparator.compare(alternativeResultList.get(i), alternativeResultList.get(i + 1)) != 0) {
                cur++;
            }
        }

        alternativeResultList.get(alternativeResultList.size() - 1).setFinalRank(cur);
    }

    private void setRelativeRanks(List<AlternativeQVResult> alternativeResultList, QuasiExpertQV qe, Map<AlternativePair, CompareType> mapCompare) {
        MyComparator<AlternativeQVResult> comparator = new AlternativeRelativeRanksQVComparator(qe);

        Map<AlternativeQVResult, Integer> map = new HashMap<>();
        for (AlternativeQVResult alternativeResult: alternativeResultList) {
            map.put(alternativeResult, 0);
        }

//        System.out.println("!!!!!! compareQV interval !!!!!!!");
        for (int i = 0; i < alternativeResultList.size(); i++) {
            for (int j = i; j < alternativeResultList.size(); j++) {
                if (i == j) continue;

                AlternativeQVResult alternativeI = alternativeResultList.get(i);
                AlternativeQVResult alternativeJ = alternativeResultList.get(j);

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

        List<Entry<AlternativeQVResult, Integer>> entries = new ArrayList<>(map.entrySet());
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

        for (AlternativeQVResult alternativeResult: alternativeResultList) {
            alternativeResult.getRelativeQVRanks().put(qe, map.get(alternativeResult));
        }
    }
}
