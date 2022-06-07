package org.polytech.zapros.comparator;

import java.util.stream.Collectors;

import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.alternative.AlternativeOrderResult;
import org.polytech.zapros.bean.alternative.CompareType;

/**
 * Компаратор для временных рангов альтернатив.
 */
public class AlternativeRelativeRanksOrderComparator implements MyComparator<AlternativeOrderResult> {

    private final QuasiExpert qe;

    public AlternativeRelativeRanksOrderComparator(QuasiExpert quasiExpert) {
        this.qe = quasiExpert;
    }

    @Override
    public CompareType compareWithType(AlternativeOrderResult o1, AlternativeOrderResult o2) {
        int size = o1.getAssessmentsRanks().get(qe).size();

        int better = 0;
        int worse = 0;
        int equal = 0;

        for (int i = 0; i < size; i++) {
            if (o1.getAssessmentsRanks().get(qe).get(i) < o2.getAssessmentsRanks().get(qe).get(i)) better++;
            else if (o1.getAssessmentsRanks().get(qe).get(i) > o2.getAssessmentsRanks().get(qe).get(i)) worse++;
            else equal++;
        }

//        System.out.println();
//        System.out.println("!!! compare ORDER: " + o1.getAlternative().getName() + " -> " + o1.getAssessmentsRanks().get(qe).stream().map(String::valueOf).collect(Collectors.joining(",")));
//        System.out.println("!!! compare ORDER: " + o2.getAlternative().getName() + " -> " + o2.getAssessmentsRanks().get(qe).stream().map(String::valueOf).collect(Collectors.joining(",")));
//        System.out.println("!!! better: " + better + ", worse: " + worse + ", equal: " + equal);

        if ((better > 0) && (worse == 0)) return CompareType.BETTER;
        else if ((better == 0) && (worse > 0)) return CompareType.WORSE;
        else if ((better == 0) && (worse == 0)) return CompareType.EQUAL;
        else return CompareType.NOT_COMPARABLE;
    }
}
