/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.keyvalue.service;

import com.luna.common.service.BaseService;
import com.luna.maintain.keyvalue.entity.KeyValue;
import com.luna.maintain.keyvalue.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class KeyValueService extends BaseService<KeyValue, Long> {

    private KeyValueRepository getKeyValueRepository() {
        return (KeyValueRepository) baseRepository;
    }


    public KeyValue findByKey(String key) {
        return getKeyValueRepository().findByKey(key);
    }

}
