/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.category.service;

import org.springframework.stereotype.Service;

import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.category.repository.BpmCategoryManager;
import com.luna.common.service.BaseService;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class BpmCategoryService extends BaseService<BpmCategory, Long> {

    private BpmCategoryManager getBpmCategoryRepository() {
        return (BpmCategoryManager) baseRepository;
    }
}
