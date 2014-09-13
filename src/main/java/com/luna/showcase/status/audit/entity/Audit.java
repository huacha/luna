/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.status.audit.entity;

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
@Table(name = "showcase_status_audit")
public class Audit extends BaseEntity<Long> implements Stateable<Stateable.AuditStatus> {

    /**
     * 标题
     */
    private String name;

    @Enumerated(EnumType.STRING)
    private AuditStatus status = AuditStatus.waiting;

    private String comment;

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
