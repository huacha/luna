package com.luna.bpm.conf.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfRule;
import com.luna.bpm.conf.repository.BpmConfNodeManager;
import com.luna.bpm.conf.repository.BpmConfRuleManager;

@Controller
@RequestMapping("bpm")
public class BpmConfRuleController {
    private BpmConfNodeManager bpmConfNodeManager;
    private BpmConfRuleManager bpmConfRuleManager;

    @RequestMapping("bpm-conf-rule-list")
    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
            Model model) {
        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
        List<BpmConfRule> bpmConfRules = bpmConfRuleManager.findByBpmConfNode(bpmConfNode);

        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
        model.addAttribute("bpmConfRules", bpmConfRules);

        return "bpm/bpm-conf-rule-list";
    }

    @RequestMapping("bpm-conf-rule-save")
    public String save(@ModelAttribute BpmConfRule bpmConfRule,
            @RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
        if ((bpmConfRule.getValue() == null)
                || "".equals(bpmConfRule.getValue())) {
            return "redirect:/bpm/bpm-conf-rule-list.do?bpmConfNodeId="
                    + bpmConfNodeId;
        }

        bpmConfRule.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
        bpmConfRuleManager.save(bpmConfRule);

        return "redirect:/bpm/bpm-conf-rule-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }

    @RequestMapping("bpm-conf-rule-remove")
    public String remove(@RequestParam("id") Long id) {
        BpmConfRule bpmConfRule = bpmConfRuleManager.findOne(id);
        Long bpmConfNodeId = bpmConfRule.getBpmConfNode().getId();
        bpmConfRuleManager.delete(bpmConfRule);

        return "redirect:/bpm/bpm-conf-rule-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }

    // ~ ======================================================================
    @Resource
    public void setBpmConfNodeManager(BpmConfNodeManager bpmConfNodeManager) {
        this.bpmConfNodeManager = bpmConfNodeManager;
    }

    @Resource
    public void setBpmConfRuleManager(BpmConfRuleManager bpmConfRuleManager) {
        this.bpmConfRuleManager = bpmConfRuleManager;
    }

}
