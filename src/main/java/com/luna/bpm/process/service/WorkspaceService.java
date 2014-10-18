package com.luna.bpm.process.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    
    /**
     * 正在运行的流程.
     * @param userId 
     */
	public List<ProcessInstance> findRunningProcessInstances(String userId) {
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().active().orderByProcessInstanceId().desc().involvedUser(userId);
        List<ProcessInstance> list = query.list();
        return list;
	}
	
	/**
     * 已结流程.
	 * @param userId 
     */
	public List<HistoricProcessInstance> findFinishedProcessInstaces(String userId) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime().desc().startedBy(userId);
        List<HistoricProcessInstance> list = query.list();
		return list;
	}
	
	
}
