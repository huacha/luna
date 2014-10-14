/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.repository.BpmConfFormManager;
import com.luna.common.service.BaseService;
import com.luna.xform.entity.FormTemplate;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmConfFormService extends BaseService<BpmConfForm, Long> {

    private BpmConfFormManager getBpmConfFormRepository() {
        return (BpmConfFormManager) baseRepository;
    }
    public BpmConfForm findUnique(FormTemplate formTemplate, BpmConfNode bpmConfNode){
    	return this.getBpmConfFormRepository().findUnique(formTemplate, bpmConfNode);
    }
}
