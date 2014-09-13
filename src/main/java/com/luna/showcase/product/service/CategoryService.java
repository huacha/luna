/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.product.service;

import com.luna.common.plugin.serivce.BaseMovableService;
import com.luna.showcase.product.entity.Category;
import com.luna.showcase.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class CategoryService extends BaseMovableService<Category, Long> {

}
