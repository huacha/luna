package com.luna.bpm.conf.entity;

// Generated by Hibernate Tools
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.luna.common.entity.BaseEntity;
import com.luna.xform.entity.FormTemplate;

/**
 * BpmConfForm .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "BPM_CONF_FORM")
public class BpmConfForm extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID")
    private BpmConfNode bpmConfNode;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FORM_ID")
    private FormTemplate formTemplate;

    @Column(name = "VALUE", length = 200)
    private String value;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "ORIGIN_VALUE", length = 200)
    private String originValue;

    @Column(name = "ORIGIN_TYPE")
    private Integer originType;

    @Column(name = "STATUS")
    private Integer status;

    public BpmConfForm() {
    }

    public FormTemplate getFormTemplate() {
		return formTemplate;
	}

	public void setFormTemplate(FormTemplate formTemplate) {
		this.formTemplate = formTemplate;
	}

	public BpmConfForm(BpmConfNode bpmConfNode, String value, Integer type,
            String originValue, Integer originType, Integer status) {
        this.bpmConfNode = bpmConfNode;
        this.value = value;
        this.type = type;
        this.originValue = originValue;
        this.originType = originType;
        this.status = status;
    }

    /** @return null. */
    public BpmConfNode getBpmConfNode() {
        return this.bpmConfNode;
    }

    /**
     * @param bpmConfNode
     *            null.
     */
    public void setBpmConfNode(BpmConfNode bpmConfNode) {
        this.bpmConfNode = bpmConfNode;
    }

    /** @return null. */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     *            null.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /** @return null. */
    public Integer getType() {
        return this.type;
    }

    /**
     * @param type
     *            null.
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /** @return null. */
    public String getOriginValue() {
        return this.originValue;
    }

    /**
     * @param originValue
     *            null.
     */
    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    /** @return null. */
    public Integer getOriginType() {
        return this.originType;
    }

    /**
     * @param originType
     *            null.
     */
    public void setOriginType(Integer originType) {
        this.originType = originType;
    }

    /** @return null. */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * @param status
     *            null.
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
