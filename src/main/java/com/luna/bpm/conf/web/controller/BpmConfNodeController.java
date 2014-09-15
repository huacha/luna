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

@Controller
@RequestMapping("bpm")
public class BpmConfNodeController {
    private BpmConfNodeManager bpmConfNodeManager;
    private BpmConfBaseManager bpmConfBaseManager;

    @RequestMapping("bpm-conf-node-list")
    public String list(@RequestParam("bpmConfBaseId") Long bpmConfBaseId,
            Model model) {
        BpmConfBase bpmConfBase = bpmConfBaseManager.findOne(bpmConfBaseId);
        List<BpmConfNode> bpmConfNodes = bpmConfNodeManager.findByBpmConfBase(bpmConfBase);

        model.addAttribute("bpmConfNodes", bpmConfNodes);

        return "bpm/bpm-conf-node-list";
    }

    // ~ ======================================================================

    @Resource
    public void setBpmConfNodeManager(BpmConfNodeManager bpmConfNodeManager) {
        this.bpmConfNodeManager = bpmConfNodeManager;
    }

    @Resource
    public void setBpmConfBaseManager(BpmConfBaseManager bpmConfBaseManager) {
        this.bpmConfBaseManager = bpmConfBaseManager;
    }

}
