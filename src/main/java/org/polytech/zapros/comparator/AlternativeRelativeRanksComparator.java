package org.polytech.zapros.comparator;

import java.util.Comparator;

import org.polytech.zapros.bean.alternative.AlternativeOrderResult;
import org.polytech.zapros.bean.QuasiExpert;

/**
 * Компаратор для временных рангов альтернатив.
 */
public class AlternativeRelativeRanksComparator implements Comparator<AlternativeOrderResult> {

    private final QuasiExpert quasiExpert;

    public AlternativeRelativeRanksComparator(QuasiExpert quasiExpert) {
        this.quasiExpert = quasiExpert;
    }

    @Override
    public int compare(AlternativeOrderResult o1, AlternativeOrderResult o2) {
        int res = 0;
        int count = o1.getAssessmentsRanks().get(quasiExpert).size();

        for (int i = 0; i < o1.getAssessmentsRanks().get(quasiExpert).size(); i++) {
            if (o1.getAssessmentsRanks().get(quasiExpert).get(i) < o2.getAssessmentsRanks().get(quasiExpert).get(i)) res--;
            else if (o1.getAssessmentsRanks().get(quasiExpert).get(i) > o2.getAssessmentsRanks().get(quasiExpert).get(i)) res++;
            else count--;
        }

        if (count == 0) return 0;
        else if (res == -count) return -1;
        else if (res == count) return 1;
        else return 0;
    }
}
