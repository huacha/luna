package com.luna.bpm.delegate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.luna.common.entity.BaseEntity;

@Entity
@Table(name = "BPM_DELEGATE_INFO")
public class DelegateInfo extends BaseEntity<Long> {
	private static final long serialVersionUID = 0L;

	@Column(name = "ASSIGNEE", length = 200)
	private String assignee;
	

	@Column(name = "ATTORNEY_TYPE", length = 1)
	private String attorneyType;
	
	@Column(name = "ATTORNEY", length = 200)
	private String attorney;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "PROCESS_DEFINITION_ID")
	private String processDefinitionId;

	@Column(name = "STATUS")
	private int status;
	
	@Transient
	private String assigneeName;
	@Transient
	private String attorneyName;

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getAttorneyName() {
		return attorneyName;
	}

	public void setAttorneyName(String attorneyName) {
		this.attorneyName = attorneyName;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAttorneyType() {
		return attorneyType;
	}

	public void setAttorneyType(String attorneyType) {
		this.attorneyType = attorneyType;
	}

	public String getAttorney() {
		return attorney;
	}

	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
