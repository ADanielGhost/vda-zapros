package org.polytech.zapros.service.buildqe;

import java.util.List;

import org.polytech.zapros.bean.Answer;
import org.polytech.zapros.bean.Criteria;
import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;

public interface BuildQesService {
    /**
     * Устанавливает все ответы для всех квазиэкспертов.
     * <p>
     * В случае, если нужен дополнительный квазиэксперт, создает его и
     * инициализирует начиная с противоречащей оценки.
     * @param answerList лист ответов, из которого мы формируем матрицы квазиэкспертов.
     * @param config
     * @return
     */
    List<QuasiExpert> build(List<Answer> answerList, List<Criteria> criteriaList, QuasiExpertConfig config);
}
