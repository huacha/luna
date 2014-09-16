package com.luna.bpm.conf.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.repository.BpmConfBaseManager;
import com.luna.bpm.conf.repository.BpmConfNodeManager;
import com.luna.bpm.conf.service.BpmConfNodeService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/node")
public class BpmConfNodeController   extends BaseCRUDController<BpmConfNode, Long> {
    private BpmConfNodeService getBpmConfNodeService() {
        return (BpmConfNodeService) baseService;
    }

	
	public BpmConfNodeController() {
		setResourceIdentity("bpm:conf:node");
    }

//    @RequestMapping("bpm-conf-node-list")
//    public String list(@RequestParam("bpmConfBaseId") Long bpmConfBaseId,
//            Model model) {
//        BpmConfBase bpmConfBase = bpmConfBaseManager.findOne(bpmConfBaseId);
//        List<BpmConfNode> bpmConfNodes = bpmConfNodeManager.findByBpmConfBase(bpmConfBase);
//
//        model.addAttribute("bpmConfNodes", bpmConfNodes);
//
//        return "bpm/bpm-conf-node-list";
//    }
}
