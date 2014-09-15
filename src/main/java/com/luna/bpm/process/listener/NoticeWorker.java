package com.luna.bpm.process.listener;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luna.bpm.conf.entity.BpmConfNotice;
import com.luna.bpm.conf.repository.BpmConfNoticeManager;
import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.repository.UserRepository;

@Component
public class NoticeWorker {
	private static Logger logger = LoggerFactory.getLogger(NoticeWorker.class);
	public static final int TYPE_ARRIVAL = 0;
	public static final int TYPE_COMPLETE = 1;
	public static final int TYPE_TIMEOUT = 2;

	@Autowired
	private BpmConfNoticeManager bpmConfNoticeManager;
	
	@Autowired
	private UserRepository userRepository;

	public void process(DelegateTask delegateTask, int arrivalType) {
		String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
		String processDefinitionId = delegateTask.getProcessDefinitionId();

		List<BpmConfNotice> bpmConfNotices = bpmConfNoticeManager.find(processDefinitionId, taskDefinitionKey);

		for (BpmConfNotice bpmConfNotice : bpmConfNotices) {
			if (arrivalType == bpmConfNotice.getType()) {
				processAction(delegateTask, bpmConfNotice);
			}
		}
	}

	public void processAction(DelegateTask delegateTask,BpmConfNotice bpmConfNotice) {

		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(delegateTask.getId());
		taskEntity.setName(delegateTask.getName());
		taskEntity.setAssigneeWithoutCascade(userRepository.findOne(
				Long.parseLong(delegateTask.getAssignee())).getUsername());
		taskEntity.setVariableLocal("initiator",
				getInitiator(userRepository, delegateTask));
		logger.debug("initiator : {}", delegateTask.getVariable("initator"));
		logger.debug("variables : {}", delegateTask.getVariables());

		String receiver = bpmConfNotice.getReceiver();
		BpmMailTemplate bpmMailTemplate = bpmConfNotice.getBpmMailTemplate();
		ExpressionManager expressionManager = Context
				.getProcessEngineConfiguration().getExpressionManager();
		User user = null;
		String subject = expressionManager
				.createExpression(bpmMailTemplate.getSubject())
				.getValue(taskEntity).toString();

		String content = expressionManager
				.createExpression(bpmMailTemplate.getContent())
				.getValue(taskEntity).toString();

		if ("任务接收人".equals(receiver)) {
			user = userRepository.findOne(Long.parseLong(delegateTask.getAssignee()));
		} else if ("流程发起人".equals(receiver)) {
			user = userRepository.findOne(Long.parseLong((String) delegateTask
					.getVariables().get("initiator")));
		} else {
			HistoricProcessInstanceEntity historicProcessInstanceEntity = Context
					.getCommandContext()
					.getHistoricProcessInstanceEntityManager()
					.findHistoricProcessInstance(
							delegateTask.getProcessInstanceId());
			user = userRepository.findOne(Long.parseLong(historicProcessInstanceEntity
					.getStartUserId()));
		}

		this.sendMail(user, subject, content);
		this.sendSiteMessage(user, subject, content);
	}

	public void sendMail(User user, String subject, String content) {
		//TODO yanyong
//		MailFacade mailFacade = ApplicationContextHelper
//				.getBean(MailFacade.class);
//		mailFacade.sendMail(user.getEmail(), subject, content);
	}

	public void sendSiteMessage(User user, String subject, String content) {
		//TODO yanyong
//		MsgConnector msgConnector = ApplicationContextHelper
//				.getBean(MsgConnector.class);
//		msgConnector.send(subject, content, String.valueOf(user.getId()), null);
	}

	public String getInitiator(UserRepository userRepository,DelegateTask delegateTask) {
		return userRepository.findOne(
				Long.parseLong((String) delegateTask.getVariables().get("initiator")))
				.getUsername();
	}
}
