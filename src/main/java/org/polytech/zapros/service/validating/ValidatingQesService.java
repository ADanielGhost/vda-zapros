package org.polytech.zapros.service.validating;

import java.util.List;

import org.polytech.zapros.bean.QuasiExpert;
import org.polytech.zapros.bean.QuasiExpertConfig;

/**
 * Сервис для проверки построенных квазиэкспертов на наличие допустимых противоречий.
 */
public interface ValidatingQesService {

    /**
     * Метод, проверяющий, не превышают ли противоречия допустимые.
     * @param qes список квазиэкспертов;
     * @param config конфиг квазиэксперта.
     * @return true, если все в порядке и можно ранжировать альтернативы и
     * false, если необходимо устранить противоречия.
     */
    boolean isQesValid(List<QuasiExpert> qes, QuasiExpertConfig config);
}
