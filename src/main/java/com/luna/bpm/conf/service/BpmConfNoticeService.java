/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfNotice;
import com.luna.bpm.conf.repository.BpmConfNoticeManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmConfNoticeService extends BaseService<BpmConfNotice, Long> {

    private BpmConfNoticeManager getBpmConfNoticeRepository() {
        return (BpmConfNoticeManager) baseRepository;
    }
    
    public List<BpmConfNotice> find(String processDefinitionId,String taskDefinitionKey){
    	return this.getBpmConfNoticeRepository().find(processDefinitionId, taskDefinitionKey);
    }
	
	public List<BpmConfNotice> findByBpmConfNode(BpmConfNode bpmConfNode){
		return this.getBpmConfNoticeRepository().findByBpmConfNode(bpmConfNode);
	}
}
