/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.process.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.bpm.process.repository.BpmMailTemplateManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmMailTemplateService extends BaseService<BpmMailTemplate, Long> {

    private BpmMailTemplateManager getBpmMailTemplateRepository() {
        return (BpmMailTemplateManager) baseRepository;
    }


	public BpmMailTemplate findByName(String fieldValue) {
		return getBpmMailTemplateRepository().findByName(fieldValue);
	}
}
