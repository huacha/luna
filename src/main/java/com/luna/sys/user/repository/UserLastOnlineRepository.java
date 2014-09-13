/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.sys.user.repository;

import com.luna.common.repository.BaseRepository;
import com.luna.sys.user.entity.UserLastOnline;

/**
 * 
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface UserLastOnlineRepository extends BaseRepository<UserLastOnline, Long> {

    UserLastOnline findByUserId(Long userId);
}
