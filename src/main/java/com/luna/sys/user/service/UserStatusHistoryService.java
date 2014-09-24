/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.sys.user.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luna.common.entity.search.Searchable;
import com.luna.common.service.BaseService;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.entity.UserStatus;
import com.luna.sys.user.entity.UserStatusHistory;

/**
 * 
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
public class UserStatusHistoryService extends BaseService<UserStatusHistory, Long> {

    public void log(User opUser, User user, UserStatus newStatus, String reason) throws Exception {
        UserStatusHistory history = new UserStatusHistory();
        history.setUser(user);
        history.setOpUser(opUser);
        history.setOpDate(new Date());
        history.setStatus(newStatus);
        history.setReason(reason);
        save(history);
    }

    public UserStatusHistory findLastHistory(final User user) {
        Searchable searchable = Searchable.newSearchable()
                .addSearchParam("user_eq", user)
                .addSort(Sort.Direction.DESC, "opDate")
                .setPage(0, 1);

        Page<UserStatusHistory> page = baseRepository.findAll(searchable);

        if (page.hasContent()) {
            return page.getContent().get(0);
        }
        return null;
    }

    public String getLastReason(User user) {
        UserStatusHistory history = findLastHistory(user);
        if (history == null) {
            return "";
        }
        return history.getReason();
    }
}
