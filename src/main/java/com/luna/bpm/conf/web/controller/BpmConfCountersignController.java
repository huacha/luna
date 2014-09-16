package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfCountersign;
import com.luna.bpm.conf.service.BpmConfCountersignService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/countersign")
public class BpmConfCountersignController   extends BaseCRUDController<BpmConfCountersign, Long> {

    private BpmConfCountersignService getBpmConfCountersignService() {
        return (BpmConfCountersignService) baseService;
    }

	
	public BpmConfCountersignController() {
		setResourceIdentity("bpm:conf:countersign");
    }

//    @RequestMapping("bpm-conf-countersign-save")
//    public String save(@ModelAttribute BpmConfCountersign bpmConfCountersign,@RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
//    	
//        BpmConfCountersign dest = bpmConfCountersignManager.findOne(bpmConfCountersign.getId());
//        beanMapper.copy(bpmConfCountersign, dest);
//        bpmConfCountersignManager.save(dest);
//
//        return "redirect:/bpm/bpm-conf-user-list.do?bpmConfNodeId="
//                + bpmConfNodeId;
//    }
}
