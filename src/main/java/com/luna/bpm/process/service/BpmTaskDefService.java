/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.process.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.process.entity.BpmTaskDef;
import com.luna.bpm.process.repository.BpmTaskDefManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmTaskDefService extends BaseService<BpmTaskDef, Long> {

    private BpmTaskDefManager getBpmTaskDefRepository() {
        return (BpmTaskDefManager) baseRepository;
    }
}
