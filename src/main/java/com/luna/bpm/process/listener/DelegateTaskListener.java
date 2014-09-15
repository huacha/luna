package com.luna.bpm.process.listener;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;

import com.luna.bpm.process.cmd.DelegateTaskCmd;
import com.luna.bpm.process.delegate.DelegateInfo;
import com.luna.bpm.process.service.DelegateService;

public class DelegateTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DelegateService delegateService;

    @Override
    public void onAssignment(DelegateTask delegateTask) throws Exception {
        String assignee = delegateTask.getAssignee();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        DelegateInfo delegateInfo = delegateService.getDelegateInfo(assignee,
                processDefinitionId);

        if (delegateInfo == null) {
            return;
        }

        String attorney = delegateInfo.getAttorney();
        new DelegateTaskCmd(delegateTask.getId(), attorney).execute(Context
                .getCommandContext());
    }

    @Resource
    public void setDelegateService(DelegateService delegateService) {
        this.delegateService = delegateService;
    }
}
