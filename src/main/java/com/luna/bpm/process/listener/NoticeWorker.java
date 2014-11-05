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
import com.luna.maintain.notification.entity.NotificationSystem;
import com.luna.maintain.notification.service.NotificationApi;
import com.luna.personal.message.entity.Message;
import com.luna.personal.message.entity.MessageContent;
import com.luna.personal.message.entity.MessageType;
import com.luna.personal.message.service.MessageApi;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.service.UserService;

@Component
public class NoticeWorker {
	private static Logger logger = LoggerFactory.getLogger(NoticeWorker.class);
	public static final int TYPE_ARRIVAL = 0;
	public static final int TYPE_COMPLETE = 1;
	public static final int TYPE_TIMEOUT = 2;

	@Autowired
	private BpmConfNoticeManager bpmConfNoticeManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationApi notificationApi;
	@Autowired
	private MessageApi messageApi;

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
		taskEntity.setAssigneeWithoutCascade(delegateTask.getAssignee());
		taskEntity.setVariableLocal("initiator",delegateTask.getVariables().get("initiator"));
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
		//String dealLink="<a href=\"{ctx}/xform/process/viewTaskForm?taskId="+delegateTask.getId()+"&taskstatus=prepare\">点击处理</a>";
		//content=content+dealLink;
		
		if ("assignee".equals(receiver)) {
			//用户接收人
			user = userService.findByUsername(delegateTask.getAssignee());
		} 
		else if ("initiator".equals(receiver)) {
			//发起人
			user = userService.findByUsername((String)delegateTask
					.getVariables().get("initiator"));
		} 
		else {
			HistoricProcessInstanceEntity historicProcessInstanceEntity = Context
					.getCommandContext()
					.getHistoricProcessInstanceEntityManager()
					.findHistoricProcessInstance(
							delegateTask.getProcessInstanceId());
			user = userService.findByUsername(historicProcessInstanceEntity.getStartUserId());
		}
		String sendUserName = (String)delegateTask.getVariable("initator");
		if(null == sendUserName || 0 == sendUserName.length()){
			sendUserName="admin";
		}
		User sendUser = userService.findByUsername(sendUserName);

		this.sendMail(sendUser, user, subject, content);
		this.sendSiteMessage(user, subject, content);
	}

	public void sendMail(User sendUser, User user, String subject, String content) {
		//TODO yanyong
		Message message = new Message();
		MessageContent messageContent = new MessageContent();
		message.setTitle(subject);
		messageContent.setContent(content);
		messageContent.setMessage(message);
		message.setContent(messageContent);
		
		message.setType(MessageType.system_message);
		
		Long[] ids = {user.getId()};
		
		messageApi.sendSystemMessage(ids, message);
		
//		MailFacade mailFacade = ApplicationContextHelper
//				.getBean(MailFacade.class);
//		mailFacade.sendMail(user.getEmail(), subject, content);
	}

	public void sendSiteMessage(User user, String subject, String content) {
		notificationApi.notify(user.getId(), NotificationSystem.system, subject, content);
	}
}
