/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.process.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.process.entity.DelegateInfo;
import com.luna.bpm.process.repository.DelegateInfoManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class DelegateInfoService extends BaseService<DelegateInfo, Long> {

    private DelegateInfoManager getDelegateInfoRepository() {
        return (DelegateInfoManager) baseRepository;
    }
}
