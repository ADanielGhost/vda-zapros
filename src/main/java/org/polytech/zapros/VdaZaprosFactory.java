package org.polytech.zapros;

import java.util.ArrayList;
import java.util.List;

import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.MethodType;
import org.polytech.zapros.service.main.VdaZaprosService;

@Component
public class VdaZaprosFactory {

    private VdaZaprosFactory() {
    }

    private static final VdaZaprosFactory factory;
    static {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        factory = context.getBean("vdaZaprosFactory", VdaZaprosFactory.class);
        context.close();
    }

    @Autowired
    @Qualifier("araceService")
    private VdaZaprosService araceService;

    @Autowired
    @Qualifier("zaprosSecondService")
    private VdaZaprosService zaprosSecondService;

    @Autowired
    @Qualifier("zaprosThirdService")
    private VdaZaprosService zaprosThirdService;

    @Autowired
    @Qualifier("araceQVService")
    private VdaZaprosService araceQVService;

    public static VdaZaprosService getService(MethodType type) {
        return factory.getVdaZaprosService(type);
    }

    public static QuasiExpertConfig getConfig(List<Criteria> criteriaList) {
        int len = factory.calculateLen(criteriaList);
        List<Integer> indexes = factory.calculateIndexes(criteriaList);
        int[][] initData = factory.calculateInitData(len, criteriaList);

        return new QuasiExpertConfig(len, indexes, initData);
    }

    private VdaZaprosService getVdaZaprosService(MethodType type) {
        switch (type) {
            case ZAPROS_II: return zaprosSecondService;
            case ZAPROS_III: return zaprosThirdService;
            case ARACE: return araceService;
            case ARACE_QV: return araceQVService;
            default: throw new IllegalStateException();
        }
    }

    private int calculateLen(List<Criteria> criteriaList) {
        return criteriaList
            .stream()
            .map(x -> x.getAssessments().size())
            .reduce(0, Integer::sum);
    }

    private List<Integer> calculateIndexes(List<Criteria> criteriaList) {
        List<Integer> result = new ArrayList<>();

        int count = 0;
        for (Criteria criteria: criteriaList) {
            result.add(count);
            count += criteria.getAssessments().size();
        }

        return result;
    }

    private int[][] calculateInitData(int len, List<Criteria> criteriaList) {
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
