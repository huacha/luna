/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.extkeyvalue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luna.common.service.BaseService;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValue;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValueCategory;
import com.luna.maintain.extkeyvalue.repository.ExtKeyValueRepository;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class ExtKeyValueService extends BaseService<ExtKeyValue, Long> {
	
	@Autowired
	private ExtKeyValueCategoryService extKeyValueCategoryService;

    private ExtKeyValueRepository getExtKeyValueRepository() {
        return (ExtKeyValueRepository) baseRepository;
    }

    public ExtKeyValueService() {
    }

    public Page<ExtKeyValue> findByExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory, Pageable pageable) {
        return getExtKeyValueRepository().findByExtKeyValueCategory(extKeyValueCategory, pageable);
    }

    Page<ExtKeyValue> findByExtKeyValueCategorys(List<ExtKeyValueCategory> extKeyValueCategorys, Pageable pageable) {
        return getExtKeyValueRepository().findByExtKeyValueCategorys(extKeyValueCategorys, pageable);
    }

    public void deleteByExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory) {
        getExtKeyValueRepository().deleteByExtKeyValueCategory(extKeyValueCategory);
    }

    public  List<ExtKeyValue> findByExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory){
    	return getExtKeyValueRepository().findByExtKeyValueCategory(extKeyValueCategory);
    }
    
    public  List<ExtKeyValue> findByExtKeyValueCategoryName(String extKeyValueCategoryName){
    	return getExtKeyValueRepository().findByExtKeyValueCategory(extKeyValueCategoryService.findByName(extKeyValueCategoryName));
    }
}
