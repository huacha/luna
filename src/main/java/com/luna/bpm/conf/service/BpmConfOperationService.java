/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.bpm.conf.repository.BpmConfOperationManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmConfOperationService extends BaseService<BpmConfOperation, Long> {

    private BpmConfOperationManager getBpmConfOperationRepository() {
        return (BpmConfOperationManager) baseRepository;
    }
    public BpmConfOperation findUnique(String value, BpmConfNode bpmConfNode){
    	return this.getBpmConfOperationRepository().findUnique(value, bpmConfNode);
    }
}
