/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.status.show.entity;

import com.luna.common.entity.BaseEntity;
import com.luna.common.plugin.entity.Stateable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 
 * <p>Date: 13-2-4 上午9:38
 * <p>Version: 1.0
 */
@Entity
@Table(name = "showcase_status_show")
public class Show extends BaseEntity<Long> implements Stateable<Stateable.ShowStatus> {

    /**
     * 标题
     */
    private String name;

    @Enumerated(EnumType.STRING)
    private ShowStatus status;

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
