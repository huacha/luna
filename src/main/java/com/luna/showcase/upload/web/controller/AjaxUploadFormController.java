/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.showcase.upload.web.controller;

import com.luna.common.Constants;
import com.luna.showcase.upload.entity.Upload;
import com.luna.showcase.upload.service.UploadService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 文件上传/下载
 * 
 * <p>Date: 13-2-11 上午8:46
 * <p>Version: 1.0
 */
@Controller
@RequestMapping(value = "showcase/upload/ajax")
public class AjaxUploadFormController {

    @Autowired
    private UploadService uploadService;

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute(Constants.OP_NAME, "新增");
        if (!model.containsAttribute("upload")) {
            model.addAttribute("upload", new Upload());
        }
        return "showcase/upload/ajax/editForm";
    }

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Upload upload, RedirectAttributes redirectAttributes) throws Exception {

        uploadService.save(upload);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建文件成功");
        return "redirect:/showcase/upload";
    }

}
