/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.extkeyvalue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luna.common.service.BaseService;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValue;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValueCategory;
import com.luna.maintain.extkeyvalue.repository.ExtKeyValueCategoryRepository;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class ExtKeyValueCategoryService extends BaseService<ExtKeyValueCategory, Long> {

    @Autowired
    private ExtKeyValueService extKeyValueService;

    private ExtKeyValueCategoryRepository getExtKeyValueCategoryRepository() {
        return (ExtKeyValueCategoryRepository) baseRepository;
    }

	public ExtKeyValueCategory findByName(String fieldValue){
		return getExtKeyValueCategoryRepository().findByName(fieldValue);
	}

    public void save(ExtKeyValueCategory extKeyValueCategory, List<ExtKeyValue> extKeyValueList) {
        save(extKeyValueCategory);
        saveOrUpdateExtKeyValue(extKeyValueCategory, extKeyValueList);
    }

    public void update(ExtKeyValueCategory extKeyValueCategory, List<ExtKeyValue> extKeyValueList) {
        update(extKeyValueCategory);
        saveOrUpdateExtKeyValue(extKeyValueCategory, extKeyValueList);
    }

    private void saveOrUpdateExtKeyValue(ExtKeyValueCategory extKeyValueCategory, List<ExtKeyValue> extKeyValueList) {
        for (ExtKeyValue extKeyValue : extKeyValueList) {
            if (extKeyValue == null) {//防止中间有跳过的
                continue;
            }
            extKeyValue.setExtKeyValueCategory(extKeyValueCategory);
            if (extKeyValue.getId() == null) {//新的
                extKeyValueService.save(extKeyValue);
            } else {
                extKeyValueService.update(extKeyValue);
            }
        }
    }
}
