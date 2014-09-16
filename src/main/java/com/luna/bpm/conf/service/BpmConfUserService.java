/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.bpm.conf.service;

import org.springframework.stereotype.Service;

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
}
