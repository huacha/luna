/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.notification.service;

import com.luna.common.service.BaseService;
import com.luna.maintain.notification.entity.NotificationTemplate;
import com.luna.maintain.notification.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Date: 13-5-22 下午2:40
 * <p>Version: 1.0
 */
@Service
public class NotificationTemplateService extends BaseService<NotificationTemplate, Long> {

    private NotificationTemplateRepository getNotificationTemplateRepository() {
        return (NotificationTemplateRepository) baseRepository;
    }


    public NotificationTemplate findByName(final String name) {
        return getNotificationTemplateRepository().findByName(name);
    }
}
