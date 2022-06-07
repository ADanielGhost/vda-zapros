package org.polytech.zapros.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.QuasiExpertQV;
import org.polytech.zapros.bean.alternative.AlternativeQVResult;
import org.polytech.zapros.bean.alternative.CompareType;

public class AlternativeRelativeRanksQVComparator implements MyComparator<AlternativeQVResult> {

    private final QuasiExpertQV quasiExpertQV;

    public AlternativeRelativeRanksQVComparator(QuasiExpertQV quasiExpertQV) {
        this.quasiExpertQV = quasiExpertQV;
    }

    @Override
    public CompareType compareWithType(AlternativeQVResult o1, AlternativeQVResult o2) {
        List<Assessment> assessmentList1 = new ArrayList<>(o1.getAlternative().getAssessments());
        List<Assessment> assessmentList2 = new ArrayList<>(o2.getAlternative().getAssessments());

        List<Integer> qvRanks1 = new ArrayList<>();
        List<Integer> qvRanks2 = new ArrayList<>();

        for (int i = 0; i < assessmentList1.size(); i++) {
            Assessment assessment1 = assessmentList1.get(i);
            Assessment assessment2 = assessmentList2.get(i);

            if (assessment1.getRank() == assessment2.getRank()) {
                qvRanks1.add(0);
                qvRanks2.add(0);
                continue;
            }

            Integer value = quasiExpertQV.getQualityVariationMap().entrySet().stream()
                .filter(x -> ((x.getKey().getI().equals(assessment1) && x.getKey().getJ().equals(assessment2)) ||
                    (x.getKey().getI().equals(assessment2) && x.getKey().getJ().equals(assessment1))))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .getValue();

            if (assessment1.getRank() < assessment2.getRank()) {
                qvRanks1.add(0);
                qvRanks2.add(value);
            } else {
                qvRanks1.add(value);
                qvRanks2.add(0);
            }
        }

        qvRanks1.sort(Comparator.naturalOrder());
        qvRanks2.sort(Comparator.naturalOrder());


        int size = qvRanks1.size();

        int better = 0;
        int worse = 0;
        int equal = 0;

        for (int i = 0; i < size; i++) {
            if (qvRanks1.get(i) < qvRanks2.get(i)) worse++;
            else if (qvRanks1.get(i) > qvRanks2.get(i)) better++;
            else equal++;
        }

        System.out.println();
        System.out.println("!!! compare: o1: " + o1.getAlternative().getName() + " -> " + qvRanks1.stream().map(String::valueOf).collect(Collectors.joining(",")));
        System.out.println("!!! compare: o2: " + o2.getAlternative().getName() + " -> " + qvRanks2.stream().map(String::valueOf).collect(Collectors.joining(",")));
        System.out.println("!!! better: " + better + ", worse: " + worse + ", " + equal);
        System.out.println();

        if ((better == 0) && (worse > 0)) return CompareType.WORSE;
        else if ((better > 0) && (worse == 0)) return CompareType.BETTER;
        else if ((better == 0) && (worse == 0)) return CompareType.EQUAL;
        else return CompareType.NOT_COMPARABLE;
    }
}
