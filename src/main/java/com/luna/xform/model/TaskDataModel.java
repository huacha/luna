package com.luna.xform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDataModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String taskName;
	private String assignee;
	private List<TaskData> datas = new ArrayList<TaskData>();
	private Date endTime;
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<TaskData> getDatas() {
		return datas;
	}
	public void setDatas(List<TaskData> datas) {
		this.datas = datas;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
