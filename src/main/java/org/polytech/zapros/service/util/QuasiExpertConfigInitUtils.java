package org.polytech.zapros.service.util;

import java.util.ArrayList;
import java.util.List;

import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpertConfig;

public class QuasiExpertConfigInitUtils {

    public static QuasiExpertConfig init(List<Criteria> criteriaList) {
        int len = calculateLen(criteriaList);
        List<Integer> indexes = calculateIndexes(criteriaList);
        int[][] initData = calculateInitData(len, criteriaList);

        return new QuasiExpertConfig(len, indexes, initData);
    }

    private static int calculateLen(List<Criteria> criteriaList) {
        return criteriaList
            .stream()
            .map(x -> x.getAssessments().size())
            .reduce(0, Integer::sum);
    }

    private static List<Integer> calculateIndexes(List<Criteria> criteriaList) {
        List<Integer> result = new ArrayList<>();

        int count = 0;
        for (Criteria criteria: criteriaList) {
            result.add(count);
            count += criteria.getAssessments().size();
        }

        return result;
    }

    private static int[][] calculateInitData(int len, List<Criteria> criteriaList) {
        int[][] result = new int[len][len];

        int cur = 0;
        for (Criteria criteria: criteriaList) {
            for (int i = 0; i < len; i++) {
                result[cur][i] = 1;
            }

            int border = cur + criteria.getAssessments().size();
            for (int i = cur + 1; i < border; i++) {
                for (int j = cur + 1; j < border; j++) {
                    if (i <= j) result[i][j] = 1;
                }
            }

            cur = border;
        }

        return result;
    }
}
