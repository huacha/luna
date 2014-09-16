package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfNotice;
import com.luna.bpm.conf.service.BpmConfNoticeService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/notice")
public class BpmConfNoticeController   extends BaseCRUDController<BpmConfNotice, Long> {
    
	private BpmConfNoticeService getBpmConfNoticeService() {
        return (BpmConfNoticeService) baseService;
    }

	
	public BpmConfNoticeController() {
		setResourceIdentity("bpm:conf:notice");
    }

//	@RequestMapping("bpm-conf-notice-list")
//	public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
//			Model model) {
//		BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
//		Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
//		List<BpmConfNotice> bpmConfNotices = bpmConfNoticeManager
//				.findByBpmConfNode(bpmConfNode);
//		List<BpmConfNotice> bpmMailTemplates = bpmMailTemplateManager
//				.findAll();
//
//		model.addAttribute("bpmConfBaseId", bpmConfBaseId);
//		model.addAttribute("bpmConfNotices", bpmConfNotices);
//		model.addAttribute("bpmMailTemplates", bpmMailTemplates);
//
//		return "bpm/bpm-conf-notice-list";
//	}


}
