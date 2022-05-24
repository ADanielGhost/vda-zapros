package org.polytech.zapros.comparator;

import java.util.Comparator;

import org.polytech.zapros.bean.AlternativeResult;
import org.polytech.zapros.bean.QuasiExpertQV;

public class AlternativeFinalRankQVComparator implements Comparator<AlternativeResult> {
    @Override
    public int compare(AlternativeResult o1, AlternativeResult o2) {
        int res = 0;
        int count = o1.getRelativeQVRanks().size();

        for (QuasiExpertQV quasiExpert: o1.getRelativeQVRanks().keySet()) {
            if (o1.getRelativeQVRanks().get(quasiExpert) < o2.getRelativeQVRanks().get(quasiExpert)) {
                res--;
            } else if (o1.getRelativeQVRanks().get(quasiExpert) > o2.getRelativeQVRanks().get(quasiExpert)) {
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
