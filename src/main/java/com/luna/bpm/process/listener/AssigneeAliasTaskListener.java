package com.luna.bpm.process.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssigneeAliasTaskListener extends DefaultTaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AssigneeAliasTaskListener.class);

    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        String assignee = delegateTask.getAssignee();
        logger.debug("assignee : {}", assignee);
        
        String processInstanceId = delegateTask.getProcessInstanceId();
        //发起人Id
        String startUserId = Context.getCommandContext()
                .getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId)
                .getStartUserId();
        
//      FIXME 此处设计有问题！！！定死了"岗位"和"常用语"两个字符串，非常无厘头。要改成我们的。
        String userId = "";
        if (assignee.startsWith("岗位")) {
        	userId = findSameLevelAssignee(startUserId);
		}else if (assignee.startsWith("常用语")) {
			userId = findUpLevelAssignee(startUserId);
		}
        logger.debug("userId : {}", userId);
        delegateTask.setAssignee(userId);
    }
    
    /**
     * 同级代理人如有多个则取第一个
     * @param startUserId
     * @return
     */
    private String findSameLevelAssignee(String startUserId) {
    	//TODO 找同级代理人
    	return "";
	}
    
    /**
     * 直属上级一般只有一个，如有多个则取第一个。
     * @param startUserId
     * @return
     */
    private String findUpLevelAssignee(String startUserId) {
    	//TODO 找上级代理人
    	return "";
    }
}
