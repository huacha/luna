package com.luna.bpm.process.listener;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.repository.BpmConfUserManager;

public class ConfUserTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
            .getLogger(ConfUserTaskListener.class);
    
    @Autowired
    private BpmConfUserManager bpmConfUserManager;

    @Transactional
    public void onCreate(DelegateTask delegateTask) throws Exception {
        List<BpmConfUser> bpmConfUsers = bpmConfUserManager
                .find(delegateTask.getProcessDefinitionId(), delegateTask
                                .getExecution().getCurrentActivityId());
        logger.debug("{}", bpmConfUsers);

        ExpressionManager expressionManager = Context
                .getProcessEngineConfiguration().getExpressionManager();

        try {
            for (BpmConfUser bpmConfUser : bpmConfUsers) {
                logger.debug("status : {}, type: {}", bpmConfUser.getStatus(),
                        bpmConfUser.getType());
                logger.debug("value : {}", bpmConfUser.getValue());

                String value = expressionManager
                        .createExpression(bpmConfUser.getValue())
                        .getValue(delegateTask).toString();

                if (bpmConfUser.getStatus() == 1) {
                    if (bpmConfUser.getType() == 0) {
                        delegateTask.setAssignee(value);
                    } else if (bpmConfUser.getType() == 1) {
                        delegateTask.addCandidateUser(value);
                    } else if (bpmConfUser.getType() == 2) {
                        delegateTask.addCandidateGroup(value);
                    }
                } else if (bpmConfUser.getStatus() == 2) {
                    if (bpmConfUser.getType() == 0) {
                        if (delegateTask.getAssignee().equals(value)) {
                            delegateTask.setAssignee(null);
                        }
                    } else if (bpmConfUser.getType() == 1) {
                        delegateTask.deleteCandidateUser(value);
                    } else if (bpmConfUser.getType() == 2) {
                        delegateTask.deleteCandidateGroup(value);
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }
    }

    @Resource
    public void setBpmConfUserManager(BpmConfUserManager bpmConfUserManager) {
        this.bpmConfUserManager = bpmConfUserManager;
    }
}
