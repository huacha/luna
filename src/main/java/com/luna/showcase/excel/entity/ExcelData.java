/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.excel.entity;

import com.luna.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * <p>Date: 13-2-4 上午9:38
 * <p>Version: 1.0
 */
@Entity
@Table(name = "showcase_excel_data")
public class ExcelData extends BaseEntity<Long> {

    @Column(name = "content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}
