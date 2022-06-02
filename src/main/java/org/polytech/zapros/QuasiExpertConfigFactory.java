package org.polytech.zapros;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.polytech.zapros.bean.Assessment;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.service.util.QuasiExpertConfigInitUtils;

public class QuasiExpertConfigFactory {
    public static QuasiExpertConfig getConfig(List<Criteria> criteriaList) {
        List<Criteria> criteriaListSorted = criteriaList.stream()
            .peek(criteria -> {
                List<Assessment> assessments = criteria.getAssessments().stream()
                    .sorted(Comparator.comparing(Assessment::getOrderId))
                    .collect(Collectors.toList());
                criteria.setAssessments(assessments);
            })
            .sorted(Comparator.comparing(Criteria::getOrderId))
            .collect(Collectors.toList());

        return QuasiExpertConfigInitUtils.init(criteriaListSorted);
    }
}
