/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.index.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.maintain.push.service.PushApi;
import com.luna.personal.calendar.service.CalendarService;
import com.luna.personal.message.service.MessageService;
import com.luna.sys.resource.entity.tmp.Menu;
import com.luna.sys.resource.service.ResourceService;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

/**
 * 
 * <p>Date: 13-1-18 下午10:15
 * <p>Version: 1.0
 */
@Controller("adminIndexController")
@RequestMapping("/admin")
public class IndexController {
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PushApi pushApi;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CalendarService calendarService;


    @RequestMapping(value = {"/{index:index;?.*}"}) //spring3.2.2 bug see  http://jinnianshilongnian.iteye.com/blog/1831408
    public String index(@CurrentUser User user, Model model) {

        List<Menu> menus = resourceService.findMenus(user);
        model.addAttribute("menus", menus);

        pushApi.offline(user.getId());
        logger.info("{} = {} , success login",user.getUsername(),user.getId());
        return "admin/index/index";
    }


    @RequestMapping(value = "/welcome")
    public String welcome(@CurrentUser User loginUser, Model model) {

        //未读消息
        Long messageUnreadCount = messageService.countUnread(loginUser.getId());
        model.addAttribute("messageUnreadCount", messageUnreadCount);

        //最近3天的日历
        model.addAttribute("calendarCount", calendarService.countRecentlyCalendar(loginUser.getId(), 2));

        return "admin/index/welcome";
    }





}
