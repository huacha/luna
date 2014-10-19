package com.luna.bpm.process.service;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class UserProcessService {
	@Autowired
	private ProcessEngine processEngine;

    /**
     * 未结流程.
     */
	public Page<HistoricProcessInstance> findRunningProcessInstances(String userId, Pageable pageable) {
		
        // TODO: 改成通过runtime表搜索，提高效率
		HistoryService historyService = processEngine.getHistoryService();

		long total = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId).unfinished().count();
		List<HistoricProcessInstance> historicProcessInstances = historyService
                .createHistoricProcessInstanceQuery().startedBy(userId)
                .unfinished()
                .listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<HistoricProcessInstance>(historicProcessInstances, pageable, total);
	}


    /**
     * 已结流程.
     */
    public Page<HistoricProcessInstance> findCompletedProcessInstances(String userId, Pageable pageable) {
        HistoryService historyService = processEngine.getHistoryService();

        long total = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId).finished().count();
		List<HistoricProcessInstance> historicProcessInstances = historyService
                .createHistoricProcessInstanceQuery().startedBy(userId)
                .finished()
                .listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

		return new PageImpl<HistoricProcessInstance>(historicProcessInstances, pageable, total);
    }

    /**
     * 参与流程.
     */
    public Page<HistoricProcessInstance> findInvolvedProcessInstances(String userId, Pageable pageable) {
        HistoryService historyService = processEngine.getHistoryService();

        // TODO: finished(), unfinished()
        long total = historyService.createHistoricProcessInstanceQuery()
                .involvedUser(userId).count();
        List<HistoricProcessInstance> historicProcessInstances = historyService
                .createHistoricProcessInstanceQuery().involvedUser(userId)
                .listPage(
						(int) pageable.getPageNumber() * pageable.getPageSize(),
						pageable.getPageSize());

        return new PageImpl<HistoricProcessInstance>(historicProcessInstances, pageable, total);
    }
    
    /**
     * 终止流程
     */
    public void terminateProcessInstanceById(String processInstanceId) {
    	 processEngine.getRuntimeService().deleteProcessInstance(
                processInstanceId, "end");
    }
}
