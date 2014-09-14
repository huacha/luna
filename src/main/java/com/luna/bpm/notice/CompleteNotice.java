package com.luna.bpm.notice;

import org.activiti.engine.delegate.DelegateTask;

public class CompleteNotice extends BaseNotice {
	public void process(DelegateTask delegateTask) {
		super.process(delegateTask, ArrivalNotice.TYPE_COMPLETE);
	}
}
