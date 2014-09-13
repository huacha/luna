/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.tree.service;

import com.luna.common.plugin.serivce.BaseTreeableService;
import com.luna.showcase.tree.entity.Tree;
import com.luna.showcase.tree.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class TreeService extends BaseTreeableService<Tree, Long> {

}
