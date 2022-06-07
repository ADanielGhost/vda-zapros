package org.polytech.zapros.comparator;

import java.util.Comparator;

import org.polytech.zapros.bean.alternative.CompareType;

public interface MyComparator<T> extends Comparator<T> {
    default int compare(T o1, T o2) {
        CompareType type = compareWithType(o1, o2);
        switch (type) {
            case BETTER: return 1;
            case WORSE: return -1;
            case EQUAL: case NOT_COMPARABLE: return 0;
            default: throw new IllegalArgumentException("For CompareType");
        }
    }

    CompareType compareWithType(T o1, T o2);
}
