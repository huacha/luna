/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.upload.repository;

import com.luna.common.repository.BaseRepository;
import com.luna.showcase.upload.entity.Upload;

/**
 * 
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface UploadRepository extends BaseRepository<Upload, Long> {

    Upload findByName(String name);

}
