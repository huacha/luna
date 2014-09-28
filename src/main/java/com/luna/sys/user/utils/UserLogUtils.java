/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.sys.user.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.luna.common.utils.IpUtils;
import com.luna.common.utils.LogUtils;

/**
 * 
 * <p>Date: 13-5-20 下午3:46
 * <p>Version: 1.0
 */
public class UserLogUtils {

    /**
     * 记录格式 [ip][用户名][操作][错误消息]
     * <p/>
     * 注意操作如下：
     * loginError 登录失败
     * loginSuccess 登录成功
     * passwordError 密码错误
     * changePassword 修改密码
     * changeStatus 修改状态
     *
     * @param username
     * @param op
     * @param msg
     */
    public static String logInfo(String username, String op, String msg) {
    	StringBuilder s = new StringBuilder();
    	s.append(LogUtils.getBlock(getIp()));
    	s.append(LogUtils.getBlock(username));
    	s.append(LogUtils.getBlock(op));
    	s.append(LogUtils.getBlock(msg));
    	return s.toString();
    }

    public static Object getIp() {
        RequestAttributes requestAttributes = null;

        try {
        	requestAttributes = RequestContextHolder.currentRequestAttributes();
        } catch (Exception e) {
            //ignore  如unit test
        }

        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            return IpUtils.getIpAddr(((ServletRequestAttributes) requestAttributes).getRequest());
        }

        return "unknown";

    }

}
