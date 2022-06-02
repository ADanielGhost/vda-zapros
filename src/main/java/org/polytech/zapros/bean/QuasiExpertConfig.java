package org.polytech.zapros.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Конфиг для всех квазиэкспертов.
 * <p>
 * Содержит общие данные для всех квазиэкспертов.
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class QuasiExpertConfig {

    private long id;

    /**
     * Длина матрицы квазиэксперта.
     * По сути - сумма количества оценок всех критерий.
     * <p>
     * К примеру, для 3-ех критериев с 3-мя оценками каждый,
     * {@code len = 9}.
     */
    private final int len;

    /**
     * Вспомогательная структура, хранящая границы оценок для квазиэксперта
     * <p> К примеру, для 3-ех критериев с 3-мя оценками каждый,
     * {@code indexes = {3, 6, 9}}.
     */
    private final List<Integer> indexes;

    /**
     * Начальная матрица для всех квазиэкспертов, содержащая оценки по умолчанию
     * (т. е. A<sub>1</sub> > A<sub>2</sub> > A<sub>3</sub>, B<sub>1</sub> > B<sub>2</sub> > ...).
     */
    private final int[][] initData;
}
