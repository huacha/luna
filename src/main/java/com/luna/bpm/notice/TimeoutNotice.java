package com.luna.bpm.notice;

import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luna.bpm.persistence.domain.BpmConfNotice;

public class TimeoutNotice extends BaseNotice {
    private static Logger logger = LoggerFactory.getLogger(TimeoutNotice.class);
    
    public void process(DelegateTask delegateTask) {
		super.process(delegateTask, ArrivalNotice.TYPE_TIMEOUT);
	}
    
    @Override
    public void processAction(DelegateTask delegateTask,
            BpmConfNotice bpmConfNotice) {
        try {
            Date dueDate = delegateTask.getDueDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dueDate);

            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            Duration duration = datatypeFactory.newDuration("-"
                    + bpmConfNotice.getDueDate());
            duration.addTo(calendar);

            Date noticeDate = calendar.getTime();
            Date now = new Date();

            if ((now.getTime() < noticeDate.getTime())
                    && ((noticeDate.getTime() - now.getTime()) < (60 * 1000))) {
            	super.processAction(delegateTask, bpmConfNotice);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
