package org.polytech.zapros.comparator;

import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.alternative.AlternativeOrderResult;
import org.polytech.zapros.bean.alternative.CompareType;

/**
 * Компаратор для временных рангов альтернатив.
 */
public class AlternativeRelativeRanksOrderComparator implements MyComparator<AlternativeOrderResult> {

    private final QuasiExpert quasiExpert;

    public AlternativeRelativeRanksOrderComparator(QuasiExpert quasiExpert) {
        this.quasiExpert = quasiExpert;
    }

    @Override
    public CompareType compareWithType(AlternativeOrderResult o1, AlternativeOrderResult o2) {
        int res = 0;
        int count = o1.getAssessmentsRanks().get(quasiExpert).size();

        for (int i = 0; i < o1.getAssessmentsRanks().get(quasiExpert).size(); i++) {
            if (o1.getAssessmentsRanks().get(quasiExpert).get(i) < o2.getAssessmentsRanks().get(quasiExpert).get(i)) res--;
            else if (o1.getAssessmentsRanks().get(quasiExpert).get(i) > o2.getAssessmentsRanks().get(quasiExpert).get(i)) res++;
            else count--;
        }

        if (count == 0) return CompareType.EQUAL;
        else if (res == -count) return CompareType.WORSE;
        else if (res == count) return CompareType.BETTER;
        else return CompareType.NOT_COMPARABLE;
    }
}
