package com.luna.bpm.process;

import java.util.ArrayList;
import java.util.List;


import org.activiti.engine.impl.persistence.entity.GroupEntity;
//import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.luna.sys.group.entity.Group;
import com.luna.sys.group.service.GroupRelationService;
import com.luna.sys.group.service.GroupService;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.service.UserService;

public class CustomGroupEntityManager extends GroupEntityManager {
    private static Logger logger = LoggerFactory
            .getLogger(CustomGroupEntityManager.class);
    
    @Autowired
    private GroupRelationService groupRelationService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Override
    public List<org.activiti.engine.identity.Group> findGroupsByUser(String userName) {
        logger.debug("findGroupsByUser : {}", userName);
        User user=userService.findByUsername(userName);
        List<org.activiti.engine.identity.Group> groups = new ArrayList<org.activiti.engine.identity.Group>();
        
        /*获取用户所在组列表*/
        List<Long> groupIds = groupRelationService.findGroupIds(user.getId());

        for (Long groupId : groupIds) {
        	Group group = groupService.findOne(groupId);

            GroupEntity groupEntity = new GroupEntity(group.getName());
            
            groupEntity.setName(group.getName());
            groupEntity.setType("用户组");
            groups.add(groupEntity);
        }

        return groups;
    }

}
