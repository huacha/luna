/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.sys.user.exception;

import com.luna.common.exception.BaseException;

/**
 * 
 * <p>Date: 13-3-11 下午8:19
 * <p>Version: 1.0
 */
public class UserException extends BaseException {

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

}
