/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.personal.notification.web.controller;

import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseController;
import com.luna.maintain.notification.entity.NotificationData;
import com.luna.maintain.notification.service.NotificationDataService;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * <p>Date: 13-7-9 下午2:01
 * <p>Version: 1.0
 */
@Controller
@RequestMapping("/admin/personal/notification")
public class NotificationController extends BaseController {

    @Autowired
    private NotificationDataService notificationDataService;

    @RequestMapping
    @PageableDefaults(value = 20, sort = "id=desc")
    public String list(@CurrentUser User user, Pageable pageable, Model model) {

        Searchable searchable = Searchable.newSearchable();
        searchable.addSearchFilter("userId", SearchOperator.eq, user.getId());

        Page<NotificationData> page = notificationDataService.findAll(pageable);

        model.addAttribute("page", page);
        if(pageable.getPageNumber() == 0) {
            notificationDataService.markReadAll(user.getId());
        }

        return viewName("list");
    }

    @RequestMapping("/markRead")
    @ResponseBody
    public String markRead(@RequestParam("id") Long id) {
        try {
			notificationDataService.markRead(id);
		} catch (Exception e) {
			log.error("",e);
		}
        return "";
    }

}
