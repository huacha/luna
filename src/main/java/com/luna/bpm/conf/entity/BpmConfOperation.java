package com.luna.bpm.conf.entity;

// Generated by Hibernate Tools
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.luna.common.entity.BaseEntity;

/**
 * BpmConfOperation .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "BPM_CONF_OPERATION")
public class BpmConfOperation extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID")
    private BpmConfNode bpmConfNode;

    @Column(name = "VALUE", length = 200)
    private String value;

    @Column(name = "PRIORITY")
    private Integer priority;

    public BpmConfOperation() {
    }

    public BpmConfOperation(BpmConfNode bpmConfNode, String value,
            Integer priority) {
        this.bpmConfNode = bpmConfNode;
        this.value = value;
        this.priority = priority;
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
