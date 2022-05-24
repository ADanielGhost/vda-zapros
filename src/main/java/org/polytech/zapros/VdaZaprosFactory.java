package org.polytech.zapros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import org.polytech.zapros.bean.MethodType;
import org.polytech.zapros.service.main.VdaZaprosService;

@Component
public class VdaZaprosFactory {

    private static final VdaZaprosFactory factory;
    static {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        factory = context.getBean("vdaZaprosFactory", VdaZaprosFactory.class);
        context.close();
    }

    public static VdaZaprosService getService(MethodType type) {
        return factory.getVdaZaprosService(type);
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

    private VdaZaprosService getVdaZaprosService(MethodType type) {
        switch (type) {
            case ZAPROS_II: return zaprosSecondService;
            case ZAPROS_III: return zaprosThirdService;
            case ARACE: return araceService;
            case ARACE_QV: return araceQVService;
            default: throw new IllegalStateException();
        }
    }
}
