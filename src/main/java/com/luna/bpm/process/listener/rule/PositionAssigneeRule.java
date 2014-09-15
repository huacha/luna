package com.luna.bpm.process.listener.rule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.luna.sys.organization.repository.OrganizationRepository;

/**
 * 获得部门最接近的对应的岗位的人的信息.
 * 
 */
public class PositionAssigneeRule implements AssigneeRule {
    private static Logger logger = LoggerFactory.getLogger(PositionAssigneeRule.class);
    @Autowired
    private OrganizationRepository orgRepository;

    public List<String> process(String value, String initiator) {
    	//TODO 需要知道他到底获取的是什么。是机构id还是用户id？？？
        return ApplicationContextHelper.getBean(OrgConnector.class)
                .getPositionUserIds(initiator, value);
    }

    public String process(String initiator) {
        return null;
    }
}
