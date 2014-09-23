package com.luna.xform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.common.web.controller.BaseCRUDController;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.service.FormTemplateService;

@Controller
@RequestMapping("/xform/template")
public class FormTemplateController extends BaseCRUDController<FormTemplate, Long> {
	
	FormTemplateService getFormTemplateService(){
		return (FormTemplateService) baseService;
	}
	
	public FormTemplateController() {
		setResourceIdentity("xform:template");
	}


}
