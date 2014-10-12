/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.delegate.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.delegate.entity.DelegateHistory;
import com.luna.bpm.delegate.repository.DelegateHistoryManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class DelegateHistoryService extends BaseService<DelegateHistory, Long> {

    private DelegateHistoryManager getDelegateHistoryRepository() {
        return (DelegateHistoryManager) baseRepository;
    }
}
