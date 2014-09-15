package com.luna.bpm.process.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.luna.bpm.process.listener.NoticeWorker;

public class SendNoticeCmd implements Command<Void> {
    private String taskId;
    @Autowired
    NoticeWorker noticeWorker;

    public SendNoticeCmd(String taskId) {
        this.taskId = taskId;
    }

    public Void execute(CommandContext commandContext) {
        TaskEntity delegateTask = commandContext.getTaskEntityManager().findTaskById(taskId);
        noticeWorker.process(delegateTask,NoticeWorker.TYPE_TIMEOUT);

        return null;
    }
}
