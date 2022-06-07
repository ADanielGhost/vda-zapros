package org.polytech.zapros.comparator;

import org.polytech.zapros.bean.alternative.CompareType;

public interface MyComparator<T> {
    CompareType compareWithType(T o1, T o2);
}
