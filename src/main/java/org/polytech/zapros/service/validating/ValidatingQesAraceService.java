package org.polytech.zapros.service.validating;

import java.util.List;

import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;

@Component
public class ValidatingQesAraceService implements ValidatingQesService {

    @Override
    public boolean isQesValid(List<QuasiExpert> qes, QuasiExpertConfig config, Double threshold) {
        for (int i = 0; i < qes.size(); i++) {
            for (int j = i; j < qes.size(); j++) {
                if (i == j) continue;

                if (!isPairQesValid(qes.get(i), qes.get(j), config, threshold)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Метод для сравнения пары квазиэкспертов.
     * @param qeI матрица квазиэксперта 1.
     * @param qeJ матрица квазиэксперта 2.
     * @param config
     * @return {@code true}, если с данной парой все в порядке.
     * <p>
     * {@code false}, если необходимо устранить противоречия.
     */
    private boolean isPairQesValid(QuasiExpert qeI, QuasiExpert qeJ, QuasiExpertConfig config, Double threshold) {
        int maxNumOfDiscrepancies = calculateMaxNumOfDiscrepancies(qeI.getMatrix(), qeJ.getMatrix(), config);
        int difference = calculateDifference(qeI.getMatrix(), qeJ.getMatrix(), config);

//        System.out.println("maxNumOfDiscrepancies:" + maxNumOfDiscrepancies);
//        System.out.println("difference: " + difference);
//        System.out.println("threshold: " + config.getThreshold());
//        System.out.println("result: " + (double) difference/maxNumOfDiscrepancies);
//        System.out.println();

        // TODO возвращать double
        return (double) difference/maxNumOfDiscrepancies < threshold;
    }

    /**
     * Рассчитывает максимальное кол-во единиц в данных матрицах.
     * @param data_1 матрица квазиэксперта 1.
     * @param data_2 матрица квазиэксперта 2.
     * @param config
     * @return максимальное кол-во единиц в данных матрицах.
     */
    private int calculateMaxNumOfDiscrepancies(int[][] data_1, int[][] data_2, QuasiExpertConfig config) {
        int res = 0;
        for (int i = 0; i < config.getLen(); i++) {
            for (int j = 0; j < config.getLen(); j++) {
                if ((data_1[i][j] == 1) || (data_2[i][j] == 1)) res++;
            }
        }
        return res;
    }

    /**
     * Рассчитывает расстояние Кемени - число различающихся значений в матрицах.
     * @param data_1 матрица квазиэксперта 1.
     * @param data_2 матрица квазиэксперта 2.
     * @param config
     * @return число различающихся значений в матрицах.
     */
    private int calculateDifference(int[][] data_1, int[][] data_2, QuasiExpertConfig config) {
        int res = 0;
        for (int i = 0; i < config.getLen(); i++) {
            for (int j = 0; j < config.getLen(); j++) {
                if (data_1[i][j] != data_2[i][j]) res++;
            }
        }
        return res;
    }
}
