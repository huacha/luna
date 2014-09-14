package com.luna.bpm.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.luna.bpm.delegate.DelegateService;
import com.luna.common.utils.SpringUtils;

/**
 * 委托任务.
 */
public class DelegateTaskCmd implements Command<Void> {
//    private static Logger logger = LoggerFactory.getLogger(DelegateTaskCmd.class);
    private String taskId;
    private String attorney;

    public DelegateTaskCmd(String taskId, String attorney) {
        this.taskId = taskId;
        this.attorney = attorney;
    }

    /**
     * 委托任务.
     * 
     */
    public Void execute(CommandContext commandContext) {
        TaskEntity task = Context.getCommandContext().getTaskEntityManager()
                .findTaskById(taskId);

        String assignee = task.getAssignee();

        if (task.getOwner() == null) {
            task.setOwner(assignee);
        }

        task.setAssignee(attorney);
        SpringUtils.getBean(DelegateService.class).saveRecord(
                assignee, attorney, taskId);

        return null;
    }
}
