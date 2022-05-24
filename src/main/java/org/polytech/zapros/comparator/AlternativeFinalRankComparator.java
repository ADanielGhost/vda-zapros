package org.polytech.zapros.comparator;

import java.util.Comparator;

import org.polytech.zapros.bean.AlternativeResult;
import org.polytech.zapros.bean.QuasiExpert;

/**
 * Компаратор для окончательных рангов альтернатив.
 */
public class AlternativeFinalRankComparator implements Comparator<AlternativeResult> {
    @Override
    public int compare(AlternativeResult o1, AlternativeResult o2) {
        int res = 0;
        int count = o1.getRelativeRanks().size();

        for (QuasiExpert quasiExpert: o1.getRelativeRanks().keySet()) {
            if (o1.getRelativeRanks().get(quasiExpert) < o2.getRelativeRanks().get(quasiExpert)) {
                res--;
            } else if (o1.getRelativeRanks().get(quasiExpert) > o2.getRelativeRanks().get(quasiExpert)) {
                res++;
            } else {
                count--;
            }
        }

        if (count == 0) return 0;
        else if (res == -count) return -1;
        else if (res == count) return 1;
        else return 0;
    }
}
