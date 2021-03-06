package com.luna.xform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.repository.BpmConfOperationManager;
import com.luna.bpm.conf.service.BpmConfUserService;
import com.luna.bpm.delegate.service.DelegateService;
import com.luna.bpm.process.cmd.RollbackTaskCmd;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.repository.BpmProcessManager;
import com.luna.sys.user.entity.User;
import com.luna.xform.model.TaskData;
import com.luna.xform.model.TaskDataModel;
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
	private ManagementService managementService;
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	FormProcessRepository formProcessRepository;
    @Autowired
    BpmProcessManager bpmProcessManager;
    @Autowired
    BpmConfOperationManager bpmConfOperationManager;
    @Autowired
    DelegateService delegateService;
	@Autowired
	BpmConfUserService bpmConfUserService;
	@Autowired
	FormDataService formDataService;

    /**
     * 启动流程
     * 
     * @param user 流程启动用户
     * @param processDefinitionId
     * @return
     */
    public ProcessInstance startWorkflow(User user, String processDefinitionId) {
        ProcessInstance processInstance = null;
        try {
            //设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(user.getUsername());
            processInstance = runtimeService.startProcessInstanceById(processDefinitionId);
            String processInstanceId = processInstance.getId();
            logger.info("启动流程 processInstanceId={}", processInstanceId);
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance;
    }
    
    /**
     * 获取第一个已激活的任务
     * @param processInstance
     * @return
     */
    public Task getFirstActiveTask(ProcessInstance processInstance) {
    	Task currTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
    	return currTask;
	}
    
    /**
     * 设置第一个任务的处理人
     * @param user
     * @param processDefinitionId
     * @param task
     * @return
     */
    public boolean setFirstTaskAssignee(User user,String processDefinitionId,Task task) {
    	List<BpmConfUser> bpmConfUsers = bpmConfUserService.find(processDefinitionId, task.getTaskDefinitionKey());
        if(0 == bpmConfUsers.size()){
            taskService.setAssignee(task.getId(), user.getUsername());
            task.setAssignee(user.getUsername());
            return true;
        }
        return false;
	}
	
	/**
	 * 找到任务节点对应的表单
	 * 
	 * @param prcessDefinitionId
	 * @param taskDefinitionKey
	 * @return
	 */
	public Long getTaskFormId(String prcessDefinitionId, String taskDefinitionKey) {
		Long fid = formProcessRepository.getTaskFormId(prcessDefinitionId, taskDefinitionKey);
		return fid;
	}

	/**
	 * 获取流程实体
	 * @param processId
	 * @return
	 */
	public BpmProcess getProcess(Long processId) {
		return bpmProcessManager.findOne(processId);
	}
	
	/**
	 * 获取流程显示的名称
	 * @param processDefinitionId
	 * @return
	 */
	public String getProcessName(String processDefinitionId) {
		return formProcessRepository.getProcessName(processDefinitionId);
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
    
    /**
     * 回退任务
     * 
     * @param taskId
     * @return
     */
    public Integer rollbackTask(String taskId) {
    	Command<Integer> cmd = new RollbackTaskCmd(taskId);
    	Integer cnt = managementService.executeCommand(cmd);
    	return cnt;
	}

    /**
     * 转办
     * 
     * @param taskId
     * @param attorney
     */
    public void doDelegate(String taskId, String attorney, User user) {
    	String assignee = user.getUsername();
    	String owner = taskService.createTaskQuery().taskId(taskId).singleResult().getOwner();
    	if (StringUtils.isBlank(owner)) {
    		taskService.setOwner(taskId, assignee);
		}
    	taskService.setAssignee(taskId, attorney);
		delegateService.saveRecord(assignee, attorney, taskId);
	}
    
    /**
     * 获取流程中已完成任务的业务数据
     * 
     * @param processInstanceId
     * @param processDefinitionId
     * @return
     * @throws Exception
     */
    public List<TaskDataModel> getHistoricTaskDataModel(String processInstanceId, String processDefinitionId) throws Exception {
    	List<HistoricTaskInstance> list = historyService
				.createHistoricTaskInstanceQuery().finished()
				.processInstanceId(processInstanceId)
				.orderByHistoricTaskInstanceEndTime().asc().list();
		
		List<TaskDataModel> ls = new ArrayList<TaskDataModel>();
		for (HistoricTaskInstance his : list) {
			String name = his.getName();
			String assignee = his.getAssignee();
			Date endTime = his.getEndTime();
			
			Map<String, Object> taskBussinessData = formDataService.findTaskBussinessData(his.getId());
			List<TaskData> transData = formDataService.translateTaskBussinessData(processDefinitionId, his.getTaskDefinitionKey(), taskBussinessData);
			
			TaskDataModel taskDataModel = new TaskDataModel();
			taskDataModel.setTaskName(name);
			taskDataModel.setDatas(transData);
			taskDataModel.setAssignee(assignee);
			taskDataModel.setEndTime(endTime);
			
			ls.add(taskDataModel);
		}
		return ls;
	}
}
