/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.extkeyvalue.entity;

import com.luna.common.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 
 * <p>Date: 13-2-19 上午9:00
 * <p>Version: 1.0
 */
@Entity
@Table(name = "maintain_extkeyvalue")
public class ExtKeyValue extends BaseEntity<Long> {

    @OneToOne(optional = true)
    @Fetch(FetchMode.SELECT)
    private ExtKeyValueCategory extKeyValueCategory;

    @NotNull(message = "not.null")
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "is_show")
    private Boolean show;

    public ExtKeyValueCategory getExtKeyValueCategory() {
        return extKeyValueCategory;
    }

    public void setExtKeyValueCategory(ExtKeyValueCategory extKeyValueCategory) {
        this.extKeyValueCategory = extKeyValueCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
