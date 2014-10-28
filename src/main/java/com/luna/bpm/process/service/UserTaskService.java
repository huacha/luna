package com.luna.bpm.process.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.luna.bpm.process.cmd.WithdrawTaskCmd;

@Component
public class UserTaskService {
	@Autowired
	private ProcessEngine processEngine;
	
	/**
	 * 所有任务.
	 */
	public Page<Task> findAllTasks(String userId, Pageable pageable) {
		TaskService taskService = processEngine.getTaskService();

		long total = taskService.createTaskQuery()
				.active().count();
		List<Task> tasks = taskService
				.createTaskQuery()
				.active()
				.listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<Task>(tasks, pageable, total);
	}

	/**
	 * 待办任务（个人任务）.
	 */
	public Page<Task> findPersonalTasks(String userId, Pageable pageable) {
		TaskService taskService = processEngine.getTaskService();

		long total = taskService.createTaskQuery().taskAssignee(userId)
				.active().count();
		List<Task> tasks = taskService
				.createTaskQuery()
				.taskAssignee(userId)
				.active()
				.listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<Task>(tasks, pageable, total);
	}

	/**
	 * 代领任务（组任务）
	 */
	public Page<Task> findGroupTasks(String userId, Pageable pageable) {
		TaskService taskService = processEngine.getTaskService();

		long total = taskService.createTaskQuery().taskCandidateUser(userId)
				.active().count();
		List<Task> tasks = taskService
				.createTaskQuery()
				.taskCandidateUser(userId)
				.active()
				.listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<Task>(tasks, pageable, total);
	}

	/**
	 * 已办任务（历史任务）.
	 */
	public Page<HistoricTaskInstance> findHistoryTasks(String userId,
			Pageable pageable) {
		HistoryService historyService = processEngine.getHistoryService();

		long total = historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(userId).finished().count();
		List<HistoricTaskInstance> historicTaskInstances = historyService
				.createHistoricTaskInstanceQuery()
				.taskAssignee(userId)
				.finished()
				.listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<HistoricTaskInstance>(historicTaskInstances,
				pageable, total);
	}

	/**
	 * 代理中的任务（代理人还未完成该任务）.
	 */

	public Page<Task> findDelegatedTasks(String userId, Pageable pageable) {
		TaskService taskService = processEngine.getTaskService();

		long total = taskService.createTaskQuery().taskOwner(userId)
				.taskDelegationState(DelegationState.PENDING).count();
		List<Task> tasks = taskService
				.createTaskQuery()
				.taskOwner(userId)
				.taskDelegationState(DelegationState.PENDING)
				.listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<Task>(tasks, pageable, total);
	}
	
	/**
	 * 领取任务
	 * @param userId 用户
	 * @param taskId 任务号
	 */
	public void claim(String userId, String taskId){
		TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);
	}
	

	/**
	 * 释放任务
	 * @param taskId 任务号
	 */
	public void unclaim(String taskId){
		TaskService taskService = processEngine.getTaskService();
        taskService.unclaim(taskId);
	}

	/**
	 * 撤销已完成任务
	 * @param userId 用户
	 * @param taskId 任务号
	 */
	public Integer revoke(String taskId){
		/*根据历史任务ID查询当前流程ID*/
		//TODO 撤销任务有bug cmd中需要传入当前活动任务的ID，时没有获取到当前活动任务的taskid
//		HistoryService historyService = processEngine.getHistoryService();
//		RuntimeService runtimeService = processEngine.getRuntimeService();
//		RepositoryService repositoryService = processEngine.getRepositoryService();
//		
//		HistoricTaskInstance taskInfo = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//		
//		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(taskInfo.getProcessDefinitionId());
//		ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(taskInfo.getProcessInstanceId()).singleResult();
//		
//		if(null == pi){
//			return -1;
//		}
//		
//		//获取流程当前节点
//		String activitiId = pi.getActivityId();
//		ActivityImpl activity = pd.findActivity(activitiId);
//		
//		HistoricProcessInstance processInfo = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInfo.getProcessInstanceId()).list().get(0);
		
//		if(null != processInfo.getEndTime()){
//			return -1;
//		}
		
		Command<Integer> cmd = new WithdrawTaskCmd(taskId);

        return processEngine.getManagementService().executeCommand(cmd);
	}
}
