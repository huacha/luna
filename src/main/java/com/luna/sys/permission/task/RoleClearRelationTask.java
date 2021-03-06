/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.sys.permission.task;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luna.common.repository.RepositoryHelper;
import com.luna.sys.permission.entity.Role;
import com.luna.sys.permission.entity.RoleResourcePermission;
import com.luna.sys.permission.service.PermissionService;
import com.luna.sys.permission.service.RoleService;
import com.luna.sys.resource.service.ResourceService;

/**
 * 清理无关联的Role-Resource/Permission关系
 * 1、Role-Resource
 * 2、Role-Permission
 * <p/>
 * 
 * <p>Date: 13-5-13 下午5:09
 * <p>Version: 1.0
 */
@Service()
public class RoleClearRelationTask {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 清除删除的角色对应的关系
     */
    public void clearDeletedRoleRelation() {

        final int PAGE_SIZE = 100;
        int pn = 0;

        Page<Role> rolePage = null;
        do {
            Pageable pageable = new PageRequest(pn++, PAGE_SIZE);
            rolePage = roleService.findAll(pageable);
            //开启新事物清除
            try {
                RoleClearRelationTask roleClearRelationTask = (RoleClearRelationTask) AopContext.currentProxy();
                roleClearRelationTask.doClear(rolePage.getContent());
            } catch (Exception e) {
                //出异常也无所谓
                log.error("clear role relation error", e);

            }
            //清空会话
            RepositoryHelper.clear();
        } while (rolePage.hasNext());
    }

    public void doClear(Collection<Role> roleColl) {

        for (Role role : roleColl) {

            boolean needUpdate = false;
            Iterator<RoleResourcePermission> iter = role.getResourcePermissions().iterator();

            while (iter.hasNext()) {
                RoleResourcePermission roleResourcePermission = iter.next();

                //如果资源不存在了 就删除
                Long resourceId = roleResourcePermission.getResourceId();
                if (!resourceService.exists(resourceId)) {
                    iter.remove();
                    needUpdate = true;
                }

                Iterator<Long> permissionIdIter = roleResourcePermission.getPermissionIds().iterator();
                while (permissionIdIter.hasNext()) {
                    Long permissionId = permissionIdIter.next();

                    if (!permissionService.exists(permissionId)) {
                        permissionIdIter.remove();
                        needUpdate = true;
                    }
                }

            }

            if (needUpdate) {
                try {
					roleService.update(role);
				} catch (Exception e) {
					log.error("",e);
				}
            }


        }

    }

}
