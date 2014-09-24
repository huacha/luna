/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.maintain.extkeyvalue.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.common.Constants;
import com.luna.common.entity.enums.BooleanEnum;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.FormModel;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValue;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValueCategory;
import com.luna.maintain.extkeyvalue.entity.ExtKeyValueType;
import com.luna.maintain.extkeyvalue.service.ExtKeyValueCategoryService;
import com.luna.maintain.extkeyvalue.service.ExtKeyValueService;

/**
 * 
 * <p>Date: 13-1-28 下午4:29
 * <p>Version: 1.0
 */
@Controller
@RequestMapping(value = "/admin/maintain/extkeyvalue/category")
public class ExtKeyValueCategoryController extends BaseCRUDController<ExtKeyValueCategory, Long> {
	private final static String innerPath = "/admin/maintain/extkeyvalue";

    private ExtKeyValueCategoryService getExtKeyValueCategoryService() {
        return (ExtKeyValueCategoryService) baseService;
    }

    @Autowired
    private ExtKeyValueService extKeyValueService;

    protected ExtKeyValueCategoryController() {
        setListAlsoSetCommonData(true);
        setResourceIdentity("maintain:extkeyvalue");
    }


    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
        model.addAttribute("typeList", ExtKeyValueType.values());
    }
    
    /**
     * 验证返回格式
     * 单个：[fieldId, 1|0, msg]
     * 多个：[[fieldId, 1|0, msg],[fieldId, 1|0, msg]]
     *
     * @param fieldId
     * @param fieldValue
     * @return
     */
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id) {
        ValidateResponse response = ValidateResponse.newInstance();

        if ("name".equals(fieldId)) {
        	ExtKeyValueCategory extKeyValueCategory = getExtKeyValueCategoryService().findByName(fieldValue);
            if (extKeyValueCategory == null || (extKeyValueCategory.getId().equals(id) && extKeyValueCategory.getName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该名称已使用");
            }
        }
        return response.result();
    }


    @RequestMapping(value = "create/discard", method = RequestMethod.POST)
    @Override
    public String create(Model model, @Valid @ModelAttribute("m") ExtKeyValueCategory extKeyValueCategory, BindingResult result, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(
            Model model,
            @Valid @ModelAttribute("extKeyValueCategory") ExtKeyValueCategory extKeyValueCategory, BindingResult result,
            @FormModel("extKeyValueList") List<ExtKeyValue> extKeyValueList,
            RedirectAttributes redirectAttributes) {

        if (hasError(extKeyValueCategory, result)) {
            return showCreateForm(model);
        }
        try {
			getExtKeyValueCategoryService().save(extKeyValueCategory, extKeyValueList);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        return redirectToUrl(null);
    }

    @Override
    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") ExtKeyValueCategory extKeyValueCategory, Model model) {
        model.addAttribute("extKeyValueList", extKeyValueService.findByExtKeyValueCategory(extKeyValueCategory, null).getContent());
        return super.showUpdateForm(extKeyValueCategory, model);
    }

    @RequestMapping(value = "{id}/update/discard", method = RequestMethod.POST)
    @Override
    public String update(Model model, @Valid @ModelAttribute("m") ExtKeyValueCategory extKeyValueCategory, BindingResult result, @RequestParam(value = "BackURL", required = false) String backURL, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable("id") ExtKeyValueCategory extKeyValueCategory) {

        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }

        setCommonData(model);
        model.addAttribute("m", extKeyValueCategory);
        model.addAttribute("extKeyValueList", extKeyValueService.findByExtKeyValueCategory(extKeyValueCategory, null).getContent());
        model.addAttribute(Constants.OP_NAME, "查看");
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute("extKeyValueCategory") ExtKeyValueCategory extKeyValueCategory, BindingResult result,
            @FormModel("extKeyValueList") List<ExtKeyValue> extKeyValueList,
            @RequestParam(value = "BackURL", required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (hasError(extKeyValueCategory, result)) {
            return showUpdateForm(extKeyValueCategory, model);
        }
        try {
			getExtKeyValueCategoryService().update(extKeyValueCategory, extKeyValueList);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        return redirectToUrl(backURL);
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
    @Override
    public String showDeleteForm(@PathVariable("id") ExtKeyValueCategory extKeyValueCategory, Model model) {
        model.addAttribute("extKeyValueList", extKeyValueService.findByExtKeyValueCategory(extKeyValueCategory, null).getContent());
        return super.showDeleteForm(extKeyValueCategory, model);
    }

    /**
     * 验证失败返回true
     *
     * @param extKeyValueCategory
     * @param result
     * @return
     */
    protected boolean hasError(ExtKeyValueCategory extKeyValueCategory, BindingResult result) {
        Assert.notNull(extKeyValueCategory);


        return result.hasErrors();
    }


    //////////////////////////////////child////////////////////////////////////

    @RequestMapping(value = "{extKeyValueCategory}/keyvalue", method = RequestMethod.GET)
    @PageableDefaults(value = Integer.MAX_VALUE, sort = "id=desc")
    public String listExtKeyValue(Model model, @PathVariable("extKeyValueCategory") Long extKeyValueCategoryId, Searchable searchable) {

        this.permissionList.assertHasViewPermission();

        searchable.addSearchFilter("extKeyValueCategory.id", SearchOperator.eq, extKeyValueCategoryId);

        model.addAttribute("page", extKeyValueService.findAll(searchable));

        return ExtKeyValueCategoryController.innerPath+"/keyvalue/list";
    }

    @RequestMapping(value = "keyvalue/create", method = RequestMethod.GET)
    public String showExtKeyValueCreateForm(Model model) {

        this.permissionList.assertHasEditPermission();

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "新增");
		if (!model.containsAttribute("extKeyValue")) {
            model.addAttribute("extKeyValue", new ExtKeyValue());
        }
        return ExtKeyValueCategoryController.innerPath+"/keyvalue/editForm";
    }

    @RequestMapping(value = "keyvalue/{id}/update", method = RequestMethod.GET)
    public String showExtKeyValueUpdateForm(
            Model model,
            @PathVariable("id") ExtKeyValue extKeyValue,
            @RequestParam(value = "copy", defaultValue = "false") boolean isCopy) {

        this.permissionList.assertHasEditPermission();

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, isCopy ? "复制" : "修改");
        if (!model.containsAttribute("extKeyValue")) {
            if (extKeyValue == null) {
            	extKeyValue = new ExtKeyValue();
            }
            if (isCopy) {
            	extKeyValue.setId(null);
            }
            model.addAttribute("extKeyValue", extKeyValue);
        }
        return ExtKeyValueCategoryController.innerPath+"/keyvalue/editForm";
    }

    @RequestMapping(value = "keyvalue/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public ExtKeyValue deleteExtKeyValue(@PathVariable("id") ExtKeyValue extKeyValue) {

        this.permissionList.assertHasEditPermission();

        try {
			extKeyValueService.delete(extKeyValue);
		} catch (Exception e) {
			log.error("",e);
		}
        return extKeyValue;
    }


    @RequestMapping(value = "keyvalue/batch/delete")
    @ResponseBody
    public Object deleteExtKeyValueInBatch(@RequestParam(value = "ids", required = false) Long[] ids) {

        this.permissionList.assertHasEditPermission();

        try {
			extKeyValueService.delete(ids);
		} catch (Exception e) {
			log.error("",e);
		}

        return redirectToUrl(null);
    }


}
