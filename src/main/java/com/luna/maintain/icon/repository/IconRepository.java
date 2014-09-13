/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.icon.repository;

import com.luna.common.repository.BaseRepository;
import com.luna.maintain.icon.entity.Icon;

/**
 * 
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface IconRepository extends BaseRepository<Icon, Long> {
    Icon findByIdentity(String identity);
}
