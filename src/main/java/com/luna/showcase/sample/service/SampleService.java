/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.sample.service;

import com.luna.common.service.BaseService;
import com.luna.showcase.sample.entity.Sample;
import com.luna.showcase.sample.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class SampleService extends BaseService<Sample, Long> {

    private SampleRepository getSampleRepository() {
        return (SampleRepository) baseRepository;
    }


    public Sample findByName(String name) {
        return getSampleRepository().findByName(name);
    }

}
