/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.notification.exception;

import com.luna.common.exception.BaseException;

/**
 * 
 * <p>Date: 13-7-8 下午5:32
 * <p>Version: 1.0
 */
public class TemplateException extends BaseException {

    public TemplateException(final String code, final Object[] args) {
        super("notification", code, args);
    }
}
