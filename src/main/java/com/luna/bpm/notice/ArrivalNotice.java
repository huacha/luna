package com.luna.bpm.notice;

import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrivalNotice extends BaseNotice{
	private static Logger logger = LoggerFactory.getLogger(ArrivalNotice.class);
	
	public void process(DelegateTask delegateTask) {
		super.process(delegateTask, ArrivalNotice.TYPE_ARRIVAL);
	}
}
