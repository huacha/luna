package com.luna.bpm.process.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luna.bpm.process.listener.rule.AssigneeRule;
import com.luna.bpm.process.listener.rule.PositionAssigneeRule;
import com.luna.bpm.process.listener.rule.RuleMatcher;
import com.luna.bpm.process.listener.rule.SuperiorAssigneeRule;

public class AssigneeAliasTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AssigneeAliasTaskListener.class);
    private Map<RuleMatcher, AssigneeRule> assigneeRuleMap = new HashMap<RuleMatcher, AssigneeRule>();

    public AssigneeAliasTaskListener() {
        SuperiorAssigneeRule superiorAssigneeRule = new SuperiorAssigneeRule();
        PositionAssigneeRule positionAssigneeRule = new PositionAssigneeRule();
        assigneeRuleMap.put(new RuleMatcher("常用语"), superiorAssigneeRule);
        assigneeRuleMap.put(new RuleMatcher("岗位"), positionAssigneeRule);
    }

    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        String assignee = delegateTask.getAssignee();
        logger.debug("assignee : {}", assignee);

        for (Map.Entry<RuleMatcher, AssigneeRule> entry : assigneeRuleMap
                .entrySet()) {
            RuleMatcher ruleMatcher = entry.getKey();

            if (!ruleMatcher.matches(assignee)) {
                continue;
            }

            String value = ruleMatcher.getValue(assignee);
            AssigneeRule assigneeRule = entry.getValue();
            logger.debug("value : {}", value);
            logger.debug("assigneeRule : {}", assigneeRule);

            if (assigneeRule instanceof SuperiorAssigneeRule) {
                this.processSuperior(delegateTask, assigneeRule, value);
            } else if (assigneeRule instanceof PositionAssigneeRule) {
                this.processPosition(delegateTask, assigneeRule, value);
            }
        }
    }

    public void processSuperior(DelegateTask delegateTask,
            AssigneeRule assigneeRule, String value) {
        String processInstanceId = delegateTask.getProcessInstanceId();
        String startUserId = Context.getCommandContext()
                .getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId)
                .getStartUserId();
        String userId = assigneeRule.process(startUserId);
        logger.debug("userId : {}", userId);
        delegateTask.setAssignee(userId);
    }

    public void processPosition(DelegateTask delegateTask,
            AssigneeRule assigneeRule, String value) {
        String processInstanceId = delegateTask.getProcessInstanceId();
        String startUserId = Context.getCommandContext()
                .getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId)
                .getStartUserId();
        List<String> userIds = assigneeRule.process(value, startUserId);
        logger.debug("userIds : {}", userIds);

        if (!userIds.isEmpty()) {
            delegateTask.setAssignee(userIds.get(0));
        }
    }

}
