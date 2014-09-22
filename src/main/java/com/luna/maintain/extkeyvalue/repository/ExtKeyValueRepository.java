/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.extkeyvalue.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.luna.common.repository.BaseRepository;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValue;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValueCategory;

/**
 * 
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface ExtKeyValueRepository extends BaseRepository<ExtKeyValue, Long> {
	

    @Query(value = "select o from ExtKeyValue o where o.extKeyValueCategory=?1")
    Page<ExtKeyValue> findByExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory, Pageable pageable);


    @Query(value = "select o from ExtKeyValue o where o.extKeyValueCategory in(?1)")
    Page<ExtKeyValue> findByExtKeyValueCategorys(List<ExtKeyValueCategory> extKeyValueCategorys, Pageable pageable);

    @Modifying
    @Query(value = "delete from ExtKeyValue where extKeyValueCategory = ?1")
    void deleteByExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory);

    @Query(value = "select o from ExtKeyValue o where o.extKeyValueCategory = ?1")
    List<ExtKeyValue> findByExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory);
}
