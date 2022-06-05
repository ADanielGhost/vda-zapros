package org.polytech.zapros.service.ranking;

import java.util.List;

import org.polytech.zapros.bean.Alternative;
import org.polytech.zapros.bean.alternative.AlternativeResult;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;

/**
 * Сервис для работы с альтернативами.
 * <p>
 * Устанавливает ранги для альтернатив.
 */
public interface AlternativeRankingService {
    /**
     * Смотри {@link org.polytech.zapros.service.main.VdaZaprosService#rankAlternatives}
     */
    List<? extends AlternativeResult> rankAlternatives(List<QuasiExpert> qes, List<Alternative> alternativeList, List<Criteria> criteriaList, QuasiExpertConfig config);
}
