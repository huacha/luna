/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.move.entity;

import com.luna.common.entity.BaseEntity;
import com.luna.common.entity.validate.group.Create;
import com.luna.common.plugin.entity.Movable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 
 * <p>Date: 13-2-4 上午9:38
 * <p>Version: 1.0
 */
@Entity
@Table(name = "showcase_moveable")
public class Move extends BaseEntity<Long> implements Movable {

    @NotNull(groups = Create.class)
    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Integer weight;


    @Column(name = "is_show")
    private Boolean show;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
