/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.notification.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.utils.PrettyTimeUtils;
import com.luna.maintain.notification.entity.NotificationData;
import com.luna.maintain.notification.entity.NotificationTemplate;
import com.luna.maintain.notification.exception.TemplateNotFoundException;
import com.luna.maintain.push.service.PushApi;

/**
 * 
 * <p>Date: 13-7-8 下午5:25
 * <p>Version: 1.0
 */
@Service
public class NotificationApiImpl implements NotificationApi {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @Autowired
    private NotificationDataService notificationDataService;

    @Autowired
    private PushApi pushApi;

    /**
     * 异步发送
     * @param userId 接收人用户编号
     * @param templateName 模板名称
     * @param context 模板需要的数据
     */
    @Async
    @Override
    public void notify(final Long userId, final String templateName, final Map<String, Object> context) {
        NotificationTemplate template = notificationTemplateService.findByName(templateName);

        if(template == null) {
            throw new TemplateNotFoundException(templateName);
        }

        NotificationData data = new NotificationData();

        data.setUserId(userId);
        data.setSystem(template.getSystem());
        data.setDate(new Date());

        String content = template.getTemplate();
        String title = template.getTitle();
        if(context != null) {
            for(String key : context.keySet()) {
                //TODO 如果量大可能有性能问题 需要调优
                title = title.replace("{" + key + "}", String.valueOf(context.get(key)));
                content = content.replace("{" + key + "}", String.valueOf(context.get(key)));
            }
        }

        data.setTitle(title);
        data.setContent(content);

        try {
			notificationDataService.save(data);
		} catch (Exception e) {
			log.error("",e);
		}

        pushApi.pushNewNotification(userId, topFiveNotification(userId));

    }

    @Override
    public List<Map<String, Object>> topFiveNotification(final Long userId) {

        List<Map<String, Object>> dataList = Lists.newArrayList();

        Searchable searchable = Searchable.newSearchable();
        searchable.addSearchFilter("userId", SearchOperator.eq, userId);
//        searchable.addSearchFilter("read", SearchOperator.eq, Boolean.FALSE);
        searchable.addSort(Sort.Direction.DESC, "id");
        searchable.setPage(0, 5);

        Page<NotificationData> page = notificationDataService.findAll(searchable);

        for(NotificationData data : page.getContent()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", data.getId());
            map.put("title", data.getTitle());
            map.put("content", data.getContent());
            map.put("read", data.getRead());
            map.put("date", PrettyTimeUtils.prettyTime(data.getDate()));
            dataList.add(map);
        }

        return dataList;
    }
}
