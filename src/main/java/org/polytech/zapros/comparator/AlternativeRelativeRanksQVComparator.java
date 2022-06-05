package org.polytech.zapros.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.QuasiExpertQV;
import org.polytech.zapros.bean.alternative.AlternativeQVResult;

public class AlternativeRelativeRanksQVComparator implements Comparator<AlternativeQVResult> {

    private final QuasiExpertQV quasiExpertQV;

    public AlternativeRelativeRanksQVComparator(QuasiExpertQV quasiExpertQV) {
        this.quasiExpertQV = quasiExpertQV;
    }

    @Override
    public int compare(AlternativeQVResult o1, AlternativeQVResult o2) {
        List<Assessment> assessmentList1 = new ArrayList<>(o1.getAlternative().getAssessments());
        List<Assessment> assessmentList2 = new ArrayList<>(o2.getAlternative().getAssessments());

        //assessmentList1.sort((Comparator.comparingInt(Assessment::getCriteriaId)));
        //assessmentList2.sort((Comparator.comparingInt(Assessment::getCriteriaId)));

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

            System.out.println("COMPARE!!!");
            System.out.println(assessment1);
            System.out.println(assessment2);
            System.out.println();
            Integer value = quasiExpertQV.getQualityVariationMap().entrySet().stream()
                .peek(x -> System.out.println("qe -> " + x.getKey().getI() + " " + x.getKey().getJ().getName()))
                .filter(x -> ((x.getKey().getI().equals(assessment1) && x.getKey().getJ().equals(assessment2)) ||
                    (x.getKey().getI().equals(assessment2) && x.getKey().getJ().equals(assessment1))))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .getValue();
            System.out.println();

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

        int res = 0;
        int count = qvRanks1.size();

        for (int i = 0; i < qvRanks1.size(); i++) {
            if (qvRanks1.get(i) < qvRanks2.get(i)) res--;
            else if (qvRanks1.get(i) > qvRanks2.get(i)) res++;
            else count--;
        }

        if (count == 0) return 0;
        else if (res == -count) return -1;
        else if (res == count) return 1;
        else return 0;
    }
}
