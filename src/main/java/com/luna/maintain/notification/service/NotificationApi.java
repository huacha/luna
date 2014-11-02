/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.notification.service;

import java.util.List;
import java.util.Map;

import com.luna.maintain.notification.entity.NotificationSystem;
import com.luna.maintain.notification.exception.TemplateNotFoundException;

/**
 * 通知接口
 * 
 * <p>Date: 13-7-8 下午5:25
 * <p>Version: 1.0
 */
public interface NotificationApi {

    /**
     * 发送通知
     * @param userId 接收人用户编号
     * @param templateName 模板名称
     * @param context 模板需要的数据
     * @throws TemplateNotFoundException 没有找到相应模板
     */
    public void notify(Long userId, String templateName, Map<String, Object> context) throws TemplateNotFoundException;

    /**
     * 发送通知
     * @param userId 接收人用户编号
     * @param content 内容
     */
    public void notify(Long userId, NotificationSystem system, String title, String content);

    /**
     *
     * id :
     * title
     * content
     * date
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> topFiveNotification(Long userId);
}
