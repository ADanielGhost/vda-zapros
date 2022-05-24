package org.polytech.zapros.bean;

public class QualityVariation {
    private final Assessment i;
    private final Assessment j;

    public QualityVariation(Assessment i, Assessment j) {
        this.i = i;
        this.j = j;
    }

    public Assessment getI() {
        return i;
    }

    public Assessment getJ() {
        return j;
    }

    public boolean equalTo(Assessment otherI, Assessment otherJ) {
        return ((i == otherI && j == otherJ) || (i == otherJ && j == otherI));
    }
}
