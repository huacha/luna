package com.luna.bpm.process.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luna.bpm.process.cmd.CompleteTaskWithCommentCmd;
import com.luna.common.utils.SpringUtils;
import com.luna.xform.repository.FormProcessRepository;

public class AutoCompleteFirstTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
            .getLogger(AutoCompleteFirstTaskListener.class);

    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        String userId = Authentication.getAuthenticatedUserId();
        String assignee = delegateTask.getAssignee();

        ProcessDefinitionEntity processDefinitionEntity = Context
                .getProcessEngineConfiguration().getProcessDefinitionCache()
                .get(delegateTask.getProcessDefinitionId());

        ActivityImpl startActivity = processDefinitionEntity.getInitial();

        if (startActivity.getOutgoingTransitions().size() != 1) {
            throw new IllegalStateException(
                    "start activity outgoing transitions cannot more than 1, now is : "
                            + startActivity.getOutgoingTransitions().size());
        }

        PvmTransition pvmTransition = startActivity.getOutgoingTransitions()
                .get(0);
        PvmActivity targetActivity = pvmTransition.getDestination();

        if (!"userTask".equals(targetActivity.getProperty("type"))) {
            logger.debug("first activity is not userTask, just skip");

            return;
        }

        if (targetActivity.getId().equals(delegateTask.getExecution().getCurrentActivityId())) {
            if ((userId != null) && userId.equals(assignee)) {
            	String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            	String processDefinitionId = delegateTask.getProcessDefinitionId();
            	FormProcessRepository formProcessRepository = (FormProcessRepository)SpringUtils.getBean("formProcessRepository");
            	Long taskFormId = formProcessRepository.getTaskFormId(processDefinitionId, taskDefinitionKey);
            	if (taskFormId != null) {
					return;
				}
                logger.info("auto complete first task : {}", delegateTask);
                new CompleteTaskWithCommentCmd(delegateTask.getId(), null,"发起流程").execute(Context.getCommandContext());
            }
        }
    }
}
