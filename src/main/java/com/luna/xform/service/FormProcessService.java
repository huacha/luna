package com.luna.xform.service;

import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	FormProcessRepository formProcessRepository;
    @Autowired
    private IdentityService identityService;
    @Autowired
    BpmProcessManager bpmProcessManager;

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
            logger.debug("start process of {bkey={}, pid={}, variables={}}", new Object[]{businessKey, processInstanceId, variables});
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
	
	public Long getTaskFormId(String taskId) {
		Long fid = formProcessRepository.getTaskFormId(taskId);
		return fid;
	}

	public BpmProcess getProcess(Long processId) {
		return bpmProcessManager.findOne(processId);
	}
	
	public void getTask(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		Map<String, Object> variables = task.getProcessVariables();
		
	}

}
