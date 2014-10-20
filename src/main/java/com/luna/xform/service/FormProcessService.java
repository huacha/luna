package com.luna.xform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.bpm.conf.repository.BpmConfOperationManager;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.repository.BpmProcessManager;
import com.luna.sys.user.entity.User;
import com.luna.xform.repository.FormProcessRepository;

@Service
public class FormProcessService {
	static Logger logger = LoggerFactory.getLogger(FormProcessService.class);
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	FormProcessRepository formProcessRepository;
    @Autowired
    BpmProcessManager bpmProcessManager;
    @Autowired
    BpmConfOperationManager bpmConfOperationManager;

    /**
     * 启动流程
     * 
     * @param user 流程启动用户
     * @param variables 流程变量
     * @param businessKey 业务主键
     * @return
     */
    public ProcessInstance startWorkflow(User user, Map<String, Object> variables, String businessKey, String processDefinitionKey) {
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(user.getId().toString());
            processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
            String processInstanceId = processInstance.getId();
            logger.info("启动流程 {businessKey={}, processInstanceId={}, variables={}}", new Object[]{businessKey, processInstanceId, variables});
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance;
    }
	
    /**
     * 通过流程id找到表单id
     * 
     * @param processId
     * @return
     */
	public Long getStartFormId(Long processId) {
		Long fid = formProcessRepository.getStartFormId(processId);
		return fid;
	}
	
	public Long getTaskFormId(String taskDefinitionKey) {
		Long fid = formProcessRepository.getTaskFormId(taskDefinitionKey);
		return fid;
	}

	public BpmProcess getProcess(Long processId) {
		return bpmProcessManager.findOne(processId);
	}
	
	public Task getTask(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}
	
	/**
	 * 获取流程处理按钮
	 * 
	 * @param processDefinitionId
	 * @param taskDefinitionKey
	 * @return
	 */
	public List<String> getButtons(String processDefinitionId, String taskDefinitionKey) {
		List<String> list = new ArrayList<String>();
		List<BpmConfOperation> confOperations = bpmConfOperationManager.findBpmConfOperations(processDefinitionId, taskDefinitionKey);
		for (BpmConfOperation bpmConfOperation : confOperations) {
			list.add(bpmConfOperation.getValue());
		}
		return list;
	}

	/**
	 * 获取上个环节
	 * 
	 * @param processDefinitionId
	 * @param activityId
	 * @return
	 */
    public List<PvmActivity> getPreviousActivities(String processDefinitionId, String activityId) {
    	ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService
                .getProcessDefinition(processDefinitionId);
        if (processDefinitionEntity == null) {
            throw new IllegalArgumentException("cannot find processDefinition : " + processDefinitionId);
        }
        ActivityImpl activity = processDefinitionEntity.findActivity(activityId);
        return this.getPreviousActivities(activity);
    }

    private List<PvmActivity> getPreviousActivities(PvmActivity pvmActivity) {
        List<PvmActivity> pvmActivities = new ArrayList<PvmActivity>();
        for (PvmTransition pvmTransition : pvmActivity.getIncomingTransitions()) {
            PvmActivity targetActivity = pvmTransition.getSource();
            if ("userTask".equals(pvmActivity.getProperty("type"))) {
                pvmActivities.add(targetActivity);
            } else {
                pvmActivities.addAll(this.getPreviousActivities(targetActivity));
            }
        }
        return pvmActivities;
    }
    
    /**
     * 获取下个环节
     * 
     * @param processDefinitionId
     * @param activityId
     * @return
     */
    public List<PvmActivity> getNextActivities(String processDefinitionId, String activityId) {
    	ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService
                .getProcessDefinition(processDefinitionId);
        if (processDefinitionEntity == null) {
            throw new IllegalArgumentException("cannot find processDefinition : " + processDefinitionId);
        }
        ActivityImpl activity = processDefinitionEntity.findActivity(activityId);
        return this.getNextActivities(activity);
    }

    private List<PvmActivity> getNextActivities(PvmActivity pvmActivity) {
        List<PvmActivity> pvmActivities = new ArrayList<PvmActivity>();
        for (PvmTransition pvmTransition : pvmActivity.getOutgoingTransitions()) {
            PvmActivity targetActivity = pvmTransition.getDestination();
            if ("userTask".equals(pvmActivity.getProperty("type"))) {
                pvmActivities.add(targetActivity);
            } else {
                pvmActivities.addAll(this.getNextActivities(targetActivity));
            }
        }
        return pvmActivities;
    }

}
