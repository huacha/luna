package com.luna.bpm.conf.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfListener;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.repository.BpmConfListenerManager;
import com.luna.bpm.conf.repository.BpmConfNodeManager;

@Controller
@RequestMapping("bpm")
public class BpmConfListenerController {
    private BpmConfNodeManager bpmConfNodeManager;
    private BpmConfListenerManager bpmConfListenerManager;

    @RequestMapping("bpm-conf-listener-list")
    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
            Model model) {
        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
        List<BpmConfListener> bpmConfListeners = bpmConfListenerManager.findByBpmConfNode(bpmConfNode);
        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
        model.addAttribute("bpmConfListeners", bpmConfListeners);

        return "bpm/bpm-conf-listener-list";
    }

    // ~ ======================================================================
    @Resource
    public void setBpmConfNodeManager(BpmConfNodeManager bpmConfNodeManager) {
        this.bpmConfNodeManager = bpmConfNodeManager;
    }

    @Resource
    public void setBpmConfListenerManager(
            BpmConfListenerManager bpmConfListenerManager) {
        this.bpmConfListenerManager = bpmConfListenerManager;
    }

}
