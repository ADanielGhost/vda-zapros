package org.polytech.zapros.service.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.AlternativeResult;
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

    private List<QuasiExpertQV> getRanksRED(List<QuasiExpert> qes, List<Criteria> criteriaList) {
        List<QuasiExpertQV> result = new ArrayList<>();

        for (QuasiExpert quasiExpert: qes) {
            Map<Assessment, Integer> weights = new HashMap<>();
            for (Assessment key: quasiExpert.getOrderedRanks().keySet()) {
                if (quasiExpert.getOrderedRanks().get(key) != 0) {
                    weights.put(key, quasiExpert.getOrderedRanks().get(key));
                }
            }

            List<Entry<Assessment, Integer>> entries = new ArrayList<>(weights.entrySet());
            entries.sort((e1, e2) -> {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v2.compareTo(v1);
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

            Map<QualityVariation, Integer> qvMap = new HashMap<>();

            for (Criteria criteria: criteriaList) {
                for (int i = 0; i < criteria.getAssessments().size(); i++) {
                    int weight = 0;
                    for (int j = i; j < criteria.getAssessments().size(); j++) {
                        if (i == j) continue;

                        weight += weights.get(criteria.getAssessments().get(j));
                        QualityVariation qualityVariation = new QualityVariation(
                            criteria.getAssessments().get(i),
                            criteria.getAssessments().get(j)
                        );
                        qvMap.put(qualityVariation, weight);
                    }
                }
            }

            List<Entry<QualityVariation, Integer>> entries1 = new ArrayList<>(qvMap.entrySet());
            entries1.sort((e1, e2) -> {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v2.compareTo(v1);
            });

            int cur1 = 1;
            for (int i = 0; i < entries1.size() - 1; i++) {
                int oldValue = entries1.get(i).getValue();
                entries1.get(i).setValue(cur1);
                if (entries1.get(i + 1).getValue().compareTo(oldValue) != 0) {
                    cur1++;
                }
            }
            entries1.get(entries1.size() - 1).setValue(cur1);
            Map<QualityVariation, Integer> collect = entries1.stream().collect(
                Collectors.toMap(Entry::getKey, Entry::getValue)
            );

            QuasiExpertQV quasiExpertQV = new QuasiExpertQV();
            quasiExpertQV.setQuasiExpert(quasiExpert);
            quasiExpertQV.setQualityVariationMap(collect);
            result.add(quasiExpertQV);
        }

        return result;
    }

    private List<QuasiExpertQV> getRanksBLUE(List<QuasiExpert> qes, List<Criteria> criteriaList) {
        qes.forEach(x -> {
            x.getOrderedRanks().forEach((k,v) -> System.out.println(k.getName() + " " + v));
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
                        map.put(qualityVariation, quasiExpert.getOrderedRanks().get(list.get(j)) - quasiExpert.getOrderedRanks().get(list.get(i)));
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
    public List<AlternativeResult> rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config) {
        List<QuasiExpertQV> qeqvs = getRanksBLUE(qes, criteriaList);
        //List<QuasiExpertQV> qeqvs = getRanksRED(qes, criteriaList);

        List<AlternativeResult> result = alternativeList.stream()
            .map(alternative -> {
                AlternativeResult alternativeResult = new AlternativeResult();
                alternativeResult.setAlternative(alternative);
                alternativeResult.setRelativeQVRanks(new HashMap<>());
                return alternativeResult;
            })
            .collect(Collectors.toList());

        for (QuasiExpertQV qeqv: qeqvs) {
            setRelativeRanks(result, qeqv);
        }
        setFinalRank(result);

        return result;
    }

    private void setFinalRank(List<AlternativeResult> alternativeResultList) {
        int cur = 1;
        Comparator<AlternativeResult> comparator = new AlternativeFinalRankQVComparator();
        alternativeResultList.sort(comparator);

        for (int i = 0; i < alternativeResultList.size() - 1; i++) {
            alternativeResultList.get(i).setFinalRank(cur);

            if (comparator.compare(alternativeResultList.get(i), alternativeResultList.get(i + 1)) != 0) {
                cur++;
            }
        }

        alternativeResultList.get(alternativeResultList.size() - 1).setFinalRank(cur);
    }

    private void setRelativeRanks(List<AlternativeResult> alternativeResultList, QuasiExpertQV qe) {
        Comparator<AlternativeResult> comparator = new AlternativeRelativeRanksQVComparator(qe);

        Map<AlternativeResult, Integer> map = new HashMap<>();
        for (AlternativeResult alternativeResult: alternativeResultList) {
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

        List<Entry<AlternativeResult, Integer>> entries = new ArrayList<>(map.entrySet());
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

        for (AlternativeResult alternativeResult: alternativeResultList) {
            alternativeResult.getRelativeQVRanks().put(qe, map.get(alternativeResult));
        }
    }
}
