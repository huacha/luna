package com.luna.xform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.luna.common.entity.BaseEntity;

@Entity
@Table(name = "FORM_TEMPLATE")
public class FormTemplate extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;

    private Integer type;
    private String name;
    private String content;
    private String code;

    @Column(name = "TYPE")
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    
    @Column(name = "NAME", length = 200)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name = "CONTENT", length = 2000)
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    
    @Column(name = "CODE", length = 50)
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
