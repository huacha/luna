package com.luna.bpm.process.listener.rule;

import java.util.Collections;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.luna.sys.organization.repository.OrganizationRepository;

/**
 * 获得指定用户的上级领导.
 * 
 */
public class SuperiorAssigneeRule implements AssigneeRule {
    private static Logger logger = LoggerFactory.getLogger(SuperiorAssigneeRule.class);
    @Autowired
    private OrganizationRepository orgRepository;

    public List<String> process(String value, String initiator) {
        return Collections.singletonList(this.process(initiator));
    }

    /**
     * 获得员工的直接上级.
     */
    public String process(String initiator) {
    	
    	//TODO 需要知道他到底获取的是什么。是机构id还是用户id？？？
        return orgConnector.getSuperiorId(initiator);
    }
}
