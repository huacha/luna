package com.luna.bpm.delegate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.luna.common.entity.BaseEntity;

@Entity
@Table(name = "BPM_DELEGATE_HISTORY")
public class DelegateHistory extends BaseEntity<Long> {
	private static final long serialVersionUID = 0L;

	@Column(name = "ASSIGNEE", length = 200)
	private String assignee;
	@Column(name = "ATTORNEY", length = 200)
	private String attorney;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "DELEGATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
	private Date delegateTime;

	@Column(name = "TASK_ID")
	private String taskId;

	@Column(name = "STATUS")
	private int status;

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAttorney() {
		return attorney;
	}

	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}

	public Date getDelegateTime() {
		return delegateTime;
	}

	public void setDelegateTime(Date delegateTime) {
		this.delegateTime = delegateTime;
	}


	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
