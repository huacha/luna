package com.luna.bpm.delegate;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;

import com.luna.bpm.delegate.entity.DelegateInfo;
import com.luna.bpm.delegate.service.DelegateService;
import com.luna.bpm.process.listener.DefaultTaskListener;

public class DelegateTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DelegateService delegateService;

    public void onAssignment(DelegateTask delegateTask) throws Exception {
        String assignee = delegateTask.getAssignee();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        DelegateInfo delegateInfo = delegateService.getDelegateInfo(assignee,
                processDefinitionId);

        if (delegateInfo == null) {
            return;
        }

        String attorney = delegateInfo.getAttorney();
        delegateTask.setAssignee(attorney);
        delegateService.saveRecord(assignee, attorney, delegateTask.getId());
    }

    @Resource
    public void setDelegateService(DelegateService delegateService) {
        this.delegateService = delegateService;
    }
}
