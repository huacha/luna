package com.luna.bpm.process.entity;

// Generated by Hibernate Tools
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.common.entity.BaseEntity;

/**
 * BpmProcess .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "BPM_PROCESS")
public class BpmProcess extends BaseEntity<Long> {
    private static final long serialVersionUID = 0L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CONF_BASE_ID")
    private BpmConfBase bpmConfBase;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private BpmCategory bpmCategory;
    
    @Column(name = "NAME", length = 200)
    private String name;
    
    @Column(name = "PRIORITY")
    private Integer priority;
    
    @Column(name = "DESCN", length = 200)
    private String descn;
    
    @Column(name = "USE_TASK_CONF")
    private Integer useTaskConf;
    
    @Column(name = "CODE", length = 64)
    private String code;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bpmProcess")
    private Set<BpmTaskDef> bpmTaskDefs = new HashSet<BpmTaskDef>(0);
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bpmProcess")
    private Set<BpmTaskDefNotice> bpmTaskDefNotices = new HashSet<BpmTaskDefNotice>(0);

    public BpmProcess() {
    }

    public BpmProcess(BpmConfBase bpmConfBase, BpmCategory bpmCategory,
            String name, Integer priority, String descn, Integer useTaskConf,
            String code, Set<BpmTaskDef> bpmTaskDefs,
            Set<BpmTaskDefNotice> bpmTaskDefNotices) {
        this.bpmConfBase = bpmConfBase;
        this.bpmCategory = bpmCategory;
        this.name = name;
        this.priority = priority;
        this.descn = descn;
        this.useTaskConf = useTaskConf;
        this.code = code;
        this.bpmTaskDefs = bpmTaskDefs;
        this.bpmTaskDefNotices = bpmTaskDefNotices;
    }

    public BpmConfBase getBpmConfBase() {
        return this.bpmConfBase;
    }
    public void setBpmConfBase(BpmConfBase bpmConfBase) {
        this.bpmConfBase = bpmConfBase;
    }
    
    public BpmCategory getBpmCategory() {
        return this.bpmCategory;
    }
    public void setBpmCategory(BpmCategory bpmCategory) {
        this.bpmCategory = bpmCategory;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getPriority() {
        return this.priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public String getDescn() {
        return this.descn;
    }
    public void setDescn(String descn) {
        this.descn = descn;
    }
    
    public Integer getUseTaskConf() {
        return this.useTaskConf;
    }
    public void setUseTaskConf(Integer useTaskConf) {
        this.useTaskConf = useTaskConf;
    }

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    
    public Set<BpmTaskDef> getBpmTaskDefs() {
        return this.bpmTaskDefs;
    }
    public void setBpmTaskDefs(Set<BpmTaskDef> bpmTaskDefs) {
        this.bpmTaskDefs = bpmTaskDefs;
    }
    
    public Set<BpmTaskDefNotice> getBpmTaskDefNotices() {
        return this.bpmTaskDefNotices;
    }
    public void setBpmTaskDefNotices(Set<BpmTaskDefNotice> bpmTaskDefNotices) {
        this.bpmTaskDefNotices = bpmTaskDefNotices;
    }
}
