/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.product.entity;

import com.luna.common.entity.BaseEntity;
import com.luna.common.plugin.entity.Movable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品类别
 * 
 * <p>Date: 13-2-19 上午8:59
 * <p>Version: 1.0
 */
@Entity
@Table(name = "showcase_category")
public class Category extends BaseEntity<Long> implements Movable {

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
