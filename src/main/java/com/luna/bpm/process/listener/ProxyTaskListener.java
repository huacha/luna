package com.luna.bpm.process.listener;

import java.util.Collections;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ProxyTaskListener implements TaskListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TaskListener> taskListeners = Collections.emptyList();

    public void notify(DelegateTask delegateTask) {
        for (TaskListener taskListener : taskListeners) {
            taskListener.notify(delegateTask);
        }
    }

    public void setTaskListeners(List<TaskListener> taskListeners) {
        this.taskListeners = taskListeners;
    }
}
