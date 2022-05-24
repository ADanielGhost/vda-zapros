package org.polytech.zapros;

import org.polytech.zapros.bean.QuasiExpertConfig;
import org.polytech.zapros.service.main.VdaZaprosService;

public class VdaZaprosWrapper {
    private final VdaZaprosService service;
    private final QuasiExpertConfig config;

    public VdaZaprosWrapper(VdaZaprosService service, QuasiExpertConfig config) {
        this.service = service;
        this.config = config;
    }

    public VdaZaprosService getService() {
        return service;
    }

    public QuasiExpertConfig getConfig() {
        return config;
    }
}
