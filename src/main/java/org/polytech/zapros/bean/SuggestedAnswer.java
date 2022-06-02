package org.polytech.zapros.bean;

import java.util.List;
import java.util.stream.Collectors;

// TODO rename
public class SuggestedAnswer {
    private Assessment i;
    private List<Assessment> j;

    public Assessment getI() {
        return i;
    }

    public void setI(Assessment i) {
        this.i = i;
    }

    public List<Assessment> getJ() {
        return j;
    }

    public void setJ(List<Assessment> j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "SuggestedAnswer{" +
            "assessmentI=" + i +
            ",assessmentJs:=" + j.stream().map(String::valueOf).collect(Collectors.joining(";")) +
            '}';
    }
}
