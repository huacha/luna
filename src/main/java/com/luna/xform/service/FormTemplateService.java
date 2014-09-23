package com.luna.xform.service;

import org.springframework.stereotype.Service;

import com.luna.common.service.BaseService;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.repository.FormTemplateRepository;

@Service
public class FormTemplateService extends BaseService<FormTemplate, Long> {

	FormTemplateRepository getFormTemplateRepository(){
		return (FormTemplateRepository)baseRepository;
	}
}
