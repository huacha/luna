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
 * BpmTaskDefNotice .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "BPM_TASK_DEF_NOTICE")
public class BpmTaskDefNotice extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;

    private BpmMailTemplate bpmMailTemplate;

    private BpmProcess bpmProcess;

    private String taskDefinitionKey;

    private Integer type;
    
    private String receiver;
    
    private String dueDate;

    public BpmTaskDefNotice() {
    }

    public BpmTaskDefNotice(BpmMailTemplate bpmMailTemplate,
            BpmProcess bpmProcess, String taskDefinitionKey, Integer type,
            String receiver, String dueDate) {
        this.bpmMailTemplate = bpmMailTemplate;
        this.bpmProcess = bpmProcess;
        this.taskDefinitionKey = taskDefinitionKey;
        this.type = type;
        this.receiver = receiver;
        this.dueDate = dueDate;
    }

    /** @return null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESS_ID")
    public BpmProcess getBpmProcess() {
        return this.bpmProcess;
    }

    /**
     * @param bpmProcess
     *            null.
     */
    public void setBpmProcess(BpmProcess bpmProcess) {
        this.bpmProcess = bpmProcess;
    }

    /** @return null. */
    @Column(name = "TASK_DEFINITION_KEY", length = 200)
    public String getTaskDefinitionKey() {
        return this.taskDefinitionKey;
    }

    /**
     * @param taskDefinitionKey
     *            null.
     */
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
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
    @Column(name = "RECEIVER", length = 200)
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
    @Column(name = "DUE_DATE", length = 50)
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
