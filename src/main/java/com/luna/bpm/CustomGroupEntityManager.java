package com.luna.bpm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luna.sys.organization.entity.Organization;
import com.luna.sys.organization.repository.OrganizationRepository;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.entity.UserOrganizationJob;
import com.luna.sys.user.repository.UserRepository;

public class CustomGroupEntityManager extends GroupEntityManager {
    private static Logger logger = LoggerFactory
            .getLogger(CustomGroupEntityManager.class);
    private UserRepository userRepository;
    private OrganizationRepository orgRepository;

    @Override
    public List<Group> findGroupsByUser(String userId) {
        logger.debug("findGroupsByUser : {}", userId);
        User user=userRepository.findOne(Long.parseLong(userId));
        List<Group> groups = new ArrayList<Group>();

        for (UserOrganizationJob orgjob : user.getOrganizationJobs()) {
        	Organization org=orgRepository.getOne(orgjob.getOrganizationId());
            GroupEntity groupEntity = new GroupEntity(String.valueOf(org.getId()));
            groupEntity.setName(org.getName());
            groupEntity.setType(org.getType().name());
            groups.add(groupEntity);
        }

        return groups;
    }

    @Resource
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Resource
    public void setOrganizationRepository(OrganizationRepository orgRepository) {
        this.orgRepository = orgRepository;
    }
}
