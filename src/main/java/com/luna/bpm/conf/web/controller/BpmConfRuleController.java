package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfRule;
import com.luna.bpm.conf.service.BpmConfRuleService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/rule")
public class BpmConfRuleController   extends BaseCRUDController<BpmConfRule, Long> {
    private BpmConfRuleService getBpmConfRuleService() {
        return (BpmConfRuleService) baseService;
    }

	
	public BpmConfRuleController() {
		setResourceIdentity("bpm:conf:rule");
    }

//    @RequestMapping("bpm-conf-rule-list")
//    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
//            Model model) {
//        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
//        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
//        List<BpmConfRule> bpmConfRules = bpmConfRuleManager.findByBpmConfNode(bpmConfNode);
//
//        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
//        model.addAttribute("bpmConfRules", bpmConfRules);
//
//        return "bpm/bpm-conf-rule-list";
//    }
//
//    @RequestMapping("bpm-conf-rule-save")
//    public String save(@ModelAttribute BpmConfRule bpmConfRule,
//            @RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
//        if ((bpmConfRule.getValue() == null)
//                || "".equals(bpmConfRule.getValue())) {
//            return "redirect:/bpm/bpm-conf-rule-list.do?bpmConfNodeId="
//                    + bpmConfNodeId;
//        }
//
//        bpmConfRule.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
//        bpmConfRuleManager.save(bpmConfRule);
//
//        return "redirect:/bpm/bpm-conf-rule-list.do?bpmConfNodeId="
//                + bpmConfNodeId;
//    }


}
