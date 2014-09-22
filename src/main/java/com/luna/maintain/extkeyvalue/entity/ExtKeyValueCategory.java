/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.extkeyvalue.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.luna.common.entity.BaseEntity;

/**
 * 
 * <p>Date: 13-2-19 上午8:59
 * <p>Version: 1.0
 */
@Entity
@Table(name = "maintain_extkeyvalue_category")
public class ExtKeyValueCategory extends BaseEntity<Long> {

    @NotNull(message = "not.null")
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ExtKeyValueType type;

    @Column(name = "is_show")
    private Boolean show;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExtKeyValueType getType() {
        return type;
    }

    public void setType(ExtKeyValueType type) {
        this.type = type;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
