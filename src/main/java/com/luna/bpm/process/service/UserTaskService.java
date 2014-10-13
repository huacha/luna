package com.luna.bpm.process.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class UserTaskService {
	@Autowired
	private ProcessEngine processEngine;

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

}
