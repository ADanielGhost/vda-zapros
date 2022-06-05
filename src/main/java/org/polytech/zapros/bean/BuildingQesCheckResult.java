package org.polytech.zapros.bean;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BuildingQesCheckResult {

    private long id;
    private boolean isOver;
    private List<QuasiExpert> qes;
    private Answer answerForReplacing;
    private List<Answer> answerList;

    public BuildingQesCheckResult(boolean isOver, List<QuasiExpert> qes) {
        this.isOver = isOver;
        this.qes = qes;
    }
}
