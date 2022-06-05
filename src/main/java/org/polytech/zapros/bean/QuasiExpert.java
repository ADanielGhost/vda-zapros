package org.polytech.zapros.bean;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Bean для квазиэксперта.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuasiExpert {
    private long id;
    private int[][] matrix;
    private Answer firstAnswer;
    private Map<Assessment, Integer> ranks;
}
