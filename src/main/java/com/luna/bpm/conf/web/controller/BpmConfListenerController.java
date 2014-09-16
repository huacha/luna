package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfListener;
import com.luna.bpm.conf.service.BpmConfListenerService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/listener")
public class BpmConfListenerController   extends BaseCRUDController<BpmConfListener, Long> {
    
	private BpmConfListenerService getBpmConfListenerService() {
        return (BpmConfListenerService) baseService;
    }

	
	public BpmConfListenerController() {
		setResourceIdentity("bpm:conf:listener");
    }

}
