/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.sys.auth.task;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.luna.common.repository.RepositoryHelper;
import com.luna.sys.auth.entity.Auth;
import com.luna.sys.auth.service.AuthService;
import com.luna.sys.group.service.GroupService;
import com.luna.sys.organization.service.JobService;
import com.luna.sys.organization.service.OrganizationService;
import com.luna.sys.permission.entity.Role;
import com.luna.sys.permission.service.RoleService;

/**
 * 清理Auth无关联的关系
 * 1、Auth-Role
 * 2、Auth-Organization/Job
 * 3、Auth-Group
 * <p/>
 * 
 * <p>Date: 13-5-18 下午1:44
 * <p>Version: 1.0
 */
@Service
public class AuthRelationClearTask {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private JobService jobService;


    /**
     * 清除删除的角色对应的关系
     */
    public void clearDeletedAuthRelation() {

        Set<Long> allRoleIds = findAllRoleIds();

        final int PAGE_SIZE = 100;
        int pn = 0;

        Page<Auth> authPage = null;

        do {
            Pageable pageable = new PageRequest(pn++, PAGE_SIZE);
            authPage = authService.findAll(pageable);
            //开启新事物清除
            try {
                AuthRelationClearTask authRelationClearService = (AuthRelationClearTask) AopContext.currentProxy();
                authRelationClearService.doClear(authPage.getContent(), allRoleIds);
            } catch (Exception e) {
                //出异常也无所谓
                log.error("clear auth relation error", e);
            }
            //清空会话
            RepositoryHelper.clear();
        } while (authPage.hasNext());
    }

    public void doClear(Collection<Auth> authColl, Set<Long> allRoleIds) {
        for (Auth auth : authColl) {
            switch (auth.getType()) {
                case user:
                    break;//因为用户是逻辑删除不用管
                case user_group:
                case organization_group:
                    if (!groupService.exists(auth.getGroupId())) {
                        try {
							authService.delete(auth);
						} catch (Exception e) {
							log.error("",e);
						}
                        continue;
                    }
                    break;
                case organization_job:
                    if (!organizationService.exists(auth.getOrganizationId())) {
                        auth.setOrganizationId(0L);
                    }
                    if (!jobService.exists(auth.getOrganizationId())) {
                        auth.setJobId(0L);
                    }
                    //如果组织机构/工作职务都为0L 那么可以删除
                    if (auth.getOrganizationId() == 0L && auth.getJobId() == 0L) {
                        try {
							authService.delete(auth);
						} catch (Exception e) {
							log.error("",e);
						}
                        continue;
                    }
                    break;
            }

            boolean hasRemovedAny = auth.getRoleIds().retainAll(allRoleIds);
            if (hasRemovedAny) {
                try {
					authService.update(auth);
				} catch (Exception e) {
					log.error("",e);
				}
            }
        }

    }

    private Set<Long> findAllRoleIds() {
        return Sets.newHashSet(Lists.transform(roleService.findAll(), new Function<Role, Long>() {
            @Override
            public Long apply(Role input) {
                return input.getId();
            }
        }));
    }


}
