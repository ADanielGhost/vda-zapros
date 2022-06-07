package org.polytech.zapros.bean.alternative;

import org.polytech.zapros.bean.Alternative;

import lombok.Getter;

@Getter
public class AlternativePair {
    private Alternative i;
    private Alternative j;

    private AlternativePair() { }
    public static AlternativePair of(Alternative i, Alternative j) {
        AlternativePair pair = new AlternativePair();
        pair.i = i;
        pair.j = j;
        return pair;
    }

    @Override
    public String toString() {
        return "AlternativePair{" + i.getName() + " & " + j.getName() + '}';
    }
}
