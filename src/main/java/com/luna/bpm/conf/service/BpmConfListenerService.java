/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfListener;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.repository.BpmConfListenerManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmConfListenerService extends BaseService<BpmConfListener, Long> {

    private BpmConfListenerManager getBpmConfListenerRepository() {
        return (BpmConfListenerManager) baseRepository;
    }
    
    public BpmConfListener findUnique(String value, int type,BpmConfNode bpmConfNode){
    	return this.getBpmConfListenerRepository().findUnique(value, type, bpmConfNode);
    }
}
