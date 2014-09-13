/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.excel.repository;

import com.luna.common.repository.BaseRepository;
import com.luna.showcase.excel.entity.ExcelData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface ExcelDataRepository extends BaseRepository<ExcelData, Long> {

    public void truncate();

    @Modifying
    @Query(value = "insert into showcase_excel_data (id, content) values(?1,?2)", nativeQuery = true)
    public void save(Long id, String content);

}
