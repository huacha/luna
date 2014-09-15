package com.luna.bpm.process.listener;

import java.util.Collections;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.luna.bpm.process.cmd.CompleteTaskWithCommentCmd;
import com.luna.bpm.conf.entity.BpmConfRule;
import com.luna.bpm.conf.repository.BpmConfRuleManager;
import com.luna.sys.organization.repository.OrganizationRepository;
import com.luna.sys.user.repository.UserRepository;

public class SkipTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
            .getLogger(SkipTaskListener.class);
    @Autowired
    BpmConfRuleManager bpmConfRuleManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository orgRepository;

    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        String processInstanceId = delegateTask.getProcessInstanceId();
        HistoricProcessInstanceEntity historicProcessInstanceEntity = Context
                .getCommandContext().getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId);

        List<BpmConfRule> bpmConfRules =bpmConfRuleManager.find(processDefinitionId, taskDefinitionKey);
        logger.debug("delegateTask.getId : {}", delegateTask.getId());
        logger.debug("taskDefinitionKey : {}", taskDefinitionKey);
        logger.debug("processDefinitionId : {}", processDefinitionId);
        logger.debug("processInstanceId : {}", processInstanceId);
        logger.debug("bpmConfRules : {}", bpmConfRules);

        ExpressionManager expressionManager = Context
                .getProcessEngineConfiguration().getExpressionManager();
        MapVariableScope mapVariableScope = new MapVariableScope();
        String initiator = historicProcessInstanceEntity.getStartUserId();
        mapVariableScope.setVariable("initiator",
        		userRepository.findOne(Long.parseLong(initiator)));

        for (BpmConfRule bpmConfRule : bpmConfRules) {
            String value = bpmConfRule.getValue();

            if ("职位".equals(value)) {
            	//TODO yanyong
//                // 获得发起人的职位
//            	int initiatorLevel = orgConnector
//                        .getJobLevelByUserId(initiator);
//
//                // 获得审批人的职位
//                int assigneeLevel = orgConnector
//                        .getJobLevelByUserId(delegateTask.getAssignee());
//
//                // 比较
//                if (initiatorLevel >= assigneeLevel) {
//                    logger.info("skip task : {}", delegateTask.getId());
//                    logger.info("initiatorLevel : {}, assigneeLevel : {}",
//                            initiatorLevel, assigneeLevel);
//                    new CompleteTaskWithCommentCmd(delegateTask.getId(),
//                            Collections.<String, Object> emptyMap(), "跳过")
//                            .execute(Context.getCommandContext());
//                }
            } else {
                Boolean result = (Boolean) expressionManager.createExpression(
                        value).getValue(mapVariableScope);

                logger.info("value : {}, result : {}", value, result);

                if (result) {
                    logger.info("skip task : {}", delegateTask.getId());
                    new CompleteTaskWithCommentCmd(delegateTask.getId(),
                            Collections.<String, Object> emptyMap(), "跳过")
                            .execute(Context.getCommandContext());
                }
            }
        }
    }
}
