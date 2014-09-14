package com.luna.bpm.persistence.domain;

// Generated by Hibernate Tools
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.luna.common.entity.BaseEntity;

/**
 * BpmConfUser .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "BPM_CONF_USER")
public class BpmConfUser extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;
    private BpmConfNode bpmConfNode;
    private String value;
    private Integer type;
    private Integer status;
    private Integer priority;

    public BpmConfUser() {
    }

    public BpmConfUser(BpmConfNode bpmConfNode, String value, Integer type,
            Integer status, Integer priority) {
        this.bpmConfNode = bpmConfNode;
        this.value = value;
        this.type = type;
        this.status = status;
        this.priority = priority;
    }

    /** @return null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID")
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
    @Column(name = "VALUE", length = 200)
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
    @Column(name = "TYPE")
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
    @Column(name = "STATUS")
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

    /** @return null. */
    @Column(name = "PRIORITY")
    public Integer getPriority() {
        return this.priority;
    }

    /**
     * @param priority
     *            null.
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
