/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.repository.BpmConfUserManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmConfUserService extends BaseService<BpmConfUser, Long> {

    private BpmConfUserManager getBpmConfUserRepository() {
        return (BpmConfUserManager) baseRepository;
    }
    
    public BpmConfUser findUnique(String value, int type, BpmConfNode bpmConfNode){
    	return this.getBpmConfUserRepository().findUnique(value, type, bpmConfNode);
    }
    
    public List<BpmConfUser> find(String processDefinitionId,String nodeCode){
    	return this.getBpmConfUserRepository().find(processDefinitionId, nodeCode);
    }
	
	public List<BpmConfUser> findByBpmConfNode(BpmConfNode bpmConfNode){
		return this.getBpmConfUserRepository().findByBpmConfNode(bpmConfNode);
		
	}
}
