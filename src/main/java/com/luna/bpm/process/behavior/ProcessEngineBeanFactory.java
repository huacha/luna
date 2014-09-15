package com.luna.bpm.process.behavior;

import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.ProcessEngineImpl;

public class ProcessEngineBeanFactory {
    private static Map<Object,Object> beanFactory;

    static {
        if (beanFactory == null) {
            ProcessEngineImpl processEngine = (ProcessEngineImpl) ProcessEngines
                    .getDefaultProcessEngine();
            beanFactory =  processEngine.getProcessEngineConfiguration().getBeans();
        }
    }

    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
        return (T) beanFactory.get(beanName);
    }
}
