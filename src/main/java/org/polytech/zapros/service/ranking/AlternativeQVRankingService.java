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
import org.polytech.zapros.bean.alternative.AlternativeQVResult;
import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.alternative.AlternativeResult;
import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QualityVariation;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.bean.QuasiExpertQV;
import org.polytech.zapros.comparator.AlternativeFinalRankQVComparator;
import org.polytech.zapros.comparator.AlternativeRelativeRanksQVComparator;

@Component
public class AlternativeQVRankingService implements AlternativeRankingService {

    private final Log log = LogFactory.getLog(this.getClass());

    private List<QuasiExpertQV> getRanksBLUE(List<QuasiExpert> qes, List<Criteria> criteriaList) {
        qes.forEach(x -> {
            x.getRanks().forEach((k, v) -> System.out.println(k.getName() + " " + v));
            System.out.println();
        });

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

        return result;
    }

    @Override
    public List<? extends AlternativeResult> rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config) {
        log.info("rankAlternatives started");
        List<QuasiExpertQV> qeqvs = getRanksBLUE(qes, criteriaList);

        List<AlternativeQVResult> result = alternativeList.stream()
            .map(alternative -> {
                AlternativeQVResult alternativeResult = new AlternativeQVResult();
                alternativeResult.setAlternative(alternative);
                alternativeResult.setRelativeQVRanks(new HashMap<>());
                return alternativeResult;
            })
            .collect(Collectors.toList());

        for (QuasiExpertQV qeqv: qeqvs) {
            setRelativeRanks(result, qeqv);
        }
        setFinalRank(result);

        log.info("rankAlternatives finished");
        return result;
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

    private void setRelativeRanks(List<AlternativeQVResult> alternativeResultList, QuasiExpertQV qe) {
        Comparator<AlternativeQVResult> comparator = new AlternativeRelativeRanksQVComparator(qe);

        Map<AlternativeQVResult, Integer> map = new HashMap<>();
        for (AlternativeQVResult alternativeResult: alternativeResultList) {
            map.put(alternativeResult, 0);
        }

        for (int i = 0; i < alternativeResultList.size(); i++) {
            for (int j = i; j < alternativeResultList.size(); j++) {
                if (i == j) continue;

                int compare = comparator.compare(alternativeResultList.get(i), alternativeResultList.get(j));
                if (compare > 0) {
                    int value = map.get(alternativeResultList.get(i));
                    value++;
                    map.put(alternativeResultList.get(i), value);
                } else if (compare < 0) {
                    int value = map.get(alternativeResultList.get(j));
                    value++;
                    map.put(alternativeResultList.get(j), value);
                } else {
                    int valueI = map.get(alternativeResultList.get(i));
                    int valueJ = map.get(alternativeResultList.get(j));
                    valueI++;
                    valueJ++;
                    map.put(alternativeResultList.get(i), valueI);
                    map.put(alternativeResultList.get(j), valueJ);
                }
            }
        }

        List<Entry<AlternativeQVResult, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort((e1, e2) -> {
            Integer v1 = e1.getValue();
            Integer v2 = e2.getValue();
            return v1.compareTo(v2);
        });

        map.forEach((k, v) -> System.out.println(k.getAlternative().getName() + " -> " + v));
        System.out.println();

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

        for (AlternativeQVResult alternativeResult: alternativeResultList) {
            alternativeResult.getRelativeQVRanks().put(qe, map.get(alternativeResult));
        }
    }
}
