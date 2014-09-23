/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfRule;
import com.luna.bpm.conf.repository.BpmConfRuleManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmConfRuleService extends BaseService<BpmConfRule, Long> {

    private BpmConfRuleManager getBpmConfRuleRepository() {
        return (BpmConfRuleManager) baseRepository;
    }
    
    public BpmConfRule findUnique(String value, BpmConfNode bpmConfNode){
    	return this.getBpmConfRuleRepository().findUnique(value, bpmConfNode);
    }
}
