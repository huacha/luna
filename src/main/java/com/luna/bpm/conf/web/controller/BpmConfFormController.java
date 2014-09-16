package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.service.BpmConfFormService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/form")
public class BpmConfFormController   extends BaseCRUDController<BpmConfForm, Long> {
    private BpmConfFormService getBpmConfFormService() {
        return (BpmConfFormService) baseService;
    }

	
	public BpmConfFormController() {
		setResourceIdentity("bpm:conf:form");
    }
	

}
