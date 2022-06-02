package org.polytech.zapros.service.qe;

import java.util.List;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.BuildingQesCheckResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpertConfig;

/**
 * Сервис для работы с квазиэкспертами.
 * <p>
 * Отвечает за построение квазиэкспертов.
 */
public interface QuasiExpertService {

    /**
     * Смотри {@link org.polytech.zapros.service.main.VdaZaprosService#buildQes}
     */
    BuildingQesCheckResult buildQes(List<Answer> answerList, QuasiExpertConfig config, List<Criteria> criteriaList, Double threshold);
}
