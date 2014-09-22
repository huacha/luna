package com.luna.bpm.process.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.conf.entity.BpmConfCountersign;
import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfListener;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.repository.BpmConfBaseManager;
import com.luna.bpm.conf.repository.BpmConfCountersignManager;
import com.luna.bpm.conf.repository.BpmConfFormManager;
import com.luna.bpm.conf.repository.BpmConfListenerManager;
import com.luna.bpm.conf.repository.BpmConfNodeManager;
import com.luna.bpm.conf.repository.BpmConfUserManager;
import com.luna.bpm.process.cmd.graph.Graph;
import com.luna.bpm.process.cmd.graph.Node;

/**
 * 把xml解析的内存模型保存到数据库里.
 */
@Component
public class SyncProcessCmd implements Command<Void> {
	/** 流程定义id. */
	private String processDefinitionId;

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Autowired
	private BpmConfBaseManager bpmConfBaseManager;
	@Autowired
	private BpmConfNodeManager bpmConfNodeManager;
	@Autowired
	private BpmConfUserManager bpmConfUserManager;
	@Autowired
	private BpmConfListenerManager bpmConfListenerManager;
	@Autowired
	private BpmConfFormManager bpmConfFormManager;
	@Autowired
	private BpmConfCountersignManager bpmConfCountersignManager;


	public Void execute(CommandContext commandContext) {
		ProcessDefinitionEntity processDefinitionEntity = new GetDeploymentProcessDefinitionCmd(
				processDefinitionId).execute(commandContext);
		String processDefinitionKey = processDefinitionEntity.getKey();
		int processDefinitionVersion = processDefinitionEntity.getVersion();
		BpmConfBase bpmConfBase = bpmConfBaseManager.findUnique(processDefinitionKey, processDefinitionVersion);

		if (bpmConfBase == null) {
			bpmConfBase = new BpmConfBase();
			bpmConfBase.setProcessDefinitionId(processDefinitionId);
			bpmConfBase.setProcessDefinitionKey(processDefinitionKey);
			bpmConfBase.setProcessDefinitionVersion(processDefinitionVersion);
			bpmConfBaseManager.save(bpmConfBase);
		} else if (bpmConfBase.getProcessDefinitionId() == null) {
			bpmConfBase.setProcessDefinitionId(processDefinitionId);
			bpmConfBaseManager.save(bpmConfBase);
		}

		BpmnModel bpmnModel = new GetBpmnModelCmd(processDefinitionId).execute(commandContext);
		Graph graph = new FindGraphCmd(processDefinitionId).execute(commandContext);
		this.processGlobal(bpmnModel, 1, bpmConfBase);

		int priority = 2;

		for (Node node : graph.getNodes()) {
			if ("exclusiveGateway".equals(node.getType())) {
				continue;
			} else if ("userTask".equals(node.getType())) {
				this.processUserTask(node, bpmnModel, priority++, bpmConfBase);
			} else if ("startEvent".equals(node.getType())) {
				this.processStartEvent(node, bpmnModel, priority++, bpmConfBase);
			} else if ("endEvent".equals(node.getType())) {
				this.processEndEvent(node, bpmnModel, priority++, bpmConfBase);
			}
		}

		return null;
	}

	/**
	 * 全局配置.
	 */
	public void processGlobal(BpmnModel bpmnModel, int priority,
			BpmConfBase bpmConfBase) {
		Process process = bpmnModel.getMainProcess();
		BpmConfNode bpmConfNode = bpmConfNodeManager.findUnique(
				process.getId(), bpmConfBase);

		if (bpmConfNode == null) {
			bpmConfNode = new BpmConfNode();
			bpmConfNode.setCode(process.getId());
			bpmConfNode.setName("全局");
			bpmConfNode.setType("process");
			bpmConfNode.setConfUser(2);
			bpmConfNode.setConfListener(0);
			bpmConfNode.setConfRule(2);
			bpmConfNode.setConfForm(0);
			bpmConfNode.setConfOperation(2);
			bpmConfNode.setConfNotice(2);
			bpmConfNode.setPriority(priority);
			bpmConfNode.setBpmConfBase(bpmConfBase);
			bpmConfNodeManager.save(bpmConfNode);
		}

		// 配置监听器
		processListener(process.getExecutionListeners(), bpmConfNode);
	}

	/**
	 * 配置用户任务.
	 */
	public void processUserTask(Node node, BpmnModel bpmnModel, int priority,
			BpmConfBase bpmConfBase) {
		BpmConfNode bpmConfNode = bpmConfNodeManager.findUnique(node.getId(),
				bpmConfBase);

		if (bpmConfNode == null) {
			bpmConfNode = new BpmConfNode();
			bpmConfNode.setCode(node.getId());
			bpmConfNode.setName(node.getName());
			bpmConfNode.setType(node.getType());
			bpmConfNode.setConfUser(0);
			bpmConfNode.setConfListener(0);
			bpmConfNode.setConfRule(0);
			bpmConfNode.setConfForm(0);
			bpmConfNode.setConfOperation(0);
			bpmConfNode.setConfNotice(0);
			bpmConfNode.setPriority(priority);
			bpmConfNode.setBpmConfBase(bpmConfBase);
			bpmConfNodeManager.save(bpmConfNode);
		}

		// 配置参与者
		UserTask userTask = (UserTask) bpmnModel.getFlowElement(node.getId());
		int index = 1;
		index = this.processUserTaskConf(bpmConfNode, userTask.getAssignee(),
				0, index);

		for (String candidateUser : userTask.getCandidateUsers()) {
			index = this.processUserTaskConf(bpmConfNode, candidateUser, 1,
					index);
		}

		for (String candidateGroup : userTask.getCandidateGroups()) {
			this.processUserTaskConf(bpmConfNode, candidateGroup, 2, index);
		}

		// 配置监听器
		this.processListener(userTask.getExecutionListeners(), bpmConfNode);
		this.processListener(userTask.getTaskListeners(), bpmConfNode);
		// 配置表单
		this.processForm(userTask, bpmConfNode);

		// 会签
		if (userTask.getLoopCharacteristics() != null) {
			BpmConfCountersign bpmConfCountersign = new BpmConfCountersign();
			bpmConfCountersign.setType(0);
			bpmConfCountersign.setRate(100);
			bpmConfCountersign.setBpmConfNode(bpmConfNode);
			bpmConfCountersign.setSequential(userTask.getLoopCharacteristics()
					.isSequential() ? 1 : 0);
			bpmConfCountersignManager.save(bpmConfCountersign);
		}
	}

	/**
	 * 配置参与者.
	 */
	public int processUserTaskConf(BpmConfNode bpmConfNode, String value,
			int type, int priority) {
		if (value == null) {
			return priority;
		}

		BpmConfUser bpmConfUser = bpmConfUserManager.findUnique(value, type,
				bpmConfNode);

		if (bpmConfUser == null) {
			bpmConfUser = new BpmConfUser();
			bpmConfUser.setValue(value);
			bpmConfUser.setType(type);
			bpmConfUser.setStatus(0);
			bpmConfUser.setPriority(priority);
			bpmConfUser.setBpmConfNode(bpmConfNode);
			bpmConfUserManager.save(bpmConfUser);
		}

		return priority + 1;
	}

	/**
	 * 配置开始事件.
	 */
	public void processStartEvent(Node node, BpmnModel bpmnModel, int priority,
			BpmConfBase bpmConfBase) {
		BpmConfNode bpmConfNode = bpmConfNodeManager.findUnique(node.getId(),
				bpmConfBase);

		if (bpmConfNode == null) {
			bpmConfNode = new BpmConfNode();
			bpmConfNode.setCode(node.getId());
			bpmConfNode.setName(node.getName());
			bpmConfNode.setType(node.getType());
			bpmConfNode.setConfUser(2);
			bpmConfNode.setConfListener(0);
			bpmConfNode.setConfRule(2);
			bpmConfNode.setConfForm(2);
			bpmConfNode.setConfOperation(2);
			bpmConfNode.setConfNotice(0);
			bpmConfNode.setPriority(priority);
			bpmConfNode.setBpmConfBase(bpmConfBase);
			bpmConfNodeManager.save(bpmConfNode);
		}

		FlowElement flowElement = bpmnModel.getFlowElement(node.getId());
		// 配置监听器
		processListener(flowElement.getExecutionListeners(), bpmConfNode);
	}

	/**
	 * 配置结束事件.
	 */
	public void processEndEvent(Node node, BpmnModel bpmnModel, int priority,
			BpmConfBase bpmConfBase) {
		BpmConfNode bpmConfNode = bpmConfNodeManager.findUnique(node.getId(),
				bpmConfBase);

		if (bpmConfNode == null) {
			bpmConfNode = new BpmConfNode();
			bpmConfNode.setCode(node.getId());
			bpmConfNode.setName(node.getName());
			bpmConfNode.setType(node.getType());
			bpmConfNode.setConfUser(2);
			bpmConfNode.setConfListener(0);
			bpmConfNode.setConfRule(2);
			bpmConfNode.setConfForm(2);
			bpmConfNode.setConfOperation(2);
			bpmConfNode.setConfNotice(0);
			bpmConfNode.setPriority(priority);
			bpmConfNode.setBpmConfBase(bpmConfBase);
			bpmConfNodeManager.save(bpmConfNode);
		}

		FlowElement flowElement = bpmnModel.getFlowElement(node.getId());
		// 配置监听器
		processListener(flowElement.getExecutionListeners(), bpmConfNode);
	}

	/**
	 * 配置监听器.
	 */
	public void processListener(List<ActivitiListener> activitiListeners,
			BpmConfNode bpmConfNode) {
		Map<String, Integer> eventTypeMap = new HashMap<String, Integer>();
		eventTypeMap.put("start", 0);
		eventTypeMap.put("end", 1);
		eventTypeMap.put("take", 2);
		eventTypeMap.put("create", 3);
		eventTypeMap.put("assignment", 4);
		eventTypeMap.put("complete", 5);
		eventTypeMap.put("delete", 6);

		for (ActivitiListener activitiListener : activitiListeners) {
			String value = activitiListener.getImplementation();
			int type = eventTypeMap.get(activitiListener.getEvent());
			BpmConfListener bpmConfListener = bpmConfListenerManager
					.findUnique(value, type, bpmConfNode);

			if (bpmConfListener == null) {
				bpmConfListener = new BpmConfListener();
				bpmConfListener.setValue(value);
				bpmConfListener.setType(type);
				bpmConfListenerManager.save(bpmConfListener);
			}
		}
	}

	/**
	 * 配置表单.
	 */
	public void processForm(UserTask userTask, BpmConfNode bpmConfNode) {
		if (userTask.getFormKey() == null) {
			return;
		}

		BpmConfForm bpmConfForm = bpmConfFormManager
				.findByBpmConfNode(bpmConfNode).get(0);

		if (bpmConfForm == null) {
			bpmConfForm = new BpmConfForm();
			bpmConfForm.setValue(userTask.getFormKey());
			bpmConfForm.setType(0);
			bpmConfForm.setOriginValue(userTask.getFormKey());
			bpmConfForm.setOriginType(0);
			bpmConfForm.setStatus(0);
			bpmConfForm.setBpmConfNode(bpmConfNode);
			bpmConfFormManager.save(bpmConfForm);
		}
	}
}
