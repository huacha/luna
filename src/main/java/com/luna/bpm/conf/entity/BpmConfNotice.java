package com.luna.bpm.conf.entity;

// Generated by Hibernate Tools
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.common.entity.BaseEntity;

/**
 * BpmConfNotice .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "BPM_CONF_NOTICE")
public class BpmConfNotice extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID")
    private BpmMailTemplate bpmMailTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID")
    private BpmConfNode bpmConfNode;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "RECEIVER", length = 200)
    private String receiver;

    @Column(name = "DUE_DATE", length = 50)
    private String dueDate;

    public BpmConfNotice() {
    }

    public BpmConfNotice(BpmMailTemplate bpmMailTemplate,
            BpmConfNode bpmConfNode, Integer type, String receiver,
            String dueDate) {
        this.bpmMailTemplate = bpmMailTemplate;
        this.bpmConfNode = bpmConfNode;
        this.type = type;
        this.receiver = receiver;
        this.dueDate = dueDate;
    }

    /** @return null. */
    public BpmMailTemplate getBpmMailTemplate() {
        return this.bpmMailTemplate;
    }

    /**
     * @param bpmMailTemplate
     *            null.
     */
    public void setBpmMailTemplate(BpmMailTemplate bpmMailTemplate) {
        this.bpmMailTemplate = bpmMailTemplate;
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
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * @param receiver
     *            null.
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /** @return null. */
    public String getDueDate() {
        return this.dueDate;
    }

    /**
     * @param dueDate
     *            null.
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
