package com.luna.bpm.conf.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.repository.BpmConfCountersignManager;
import com.luna.bpm.conf.repository.BpmConfNodeManager;
import com.luna.bpm.conf.repository.BpmConfUserManager;

@Controller
@RequestMapping("bpm")
public class BpmConfUserController {
    private BpmConfNodeManager bpmConfNodeManager;
    private BpmConfUserManager bpmConfUserManager;
    private BpmConfCountersignManager bpmConfCountersignManager;

    @RequestMapping("bpm-conf-user-list")
    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
            Model model) {
        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
        List<BpmConfUser> bpmConfUsers = bpmConfUserManager.findByBpmConfNode(bpmConfNode);

        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
        model.addAttribute("bpmConfUsers", bpmConfUsers);
        model.addAttribute("bpmConfCountersign", bpmConfCountersignManager.findByBpmConfNode( bpmConfNode));

        return "bpm/bpm-conf-user-list";
    }

    @RequestMapping("bpm-conf-user-save")
    public String save(@ModelAttribute BpmConfUser bpmConfUser,
            @RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
        bpmConfUser.setPriority(0);
        bpmConfUser.setStatus(1);
        bpmConfUser.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
        bpmConfUserManager.save(bpmConfUser);

        return "redirect:/bpm/bpm-conf-user-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }

    @RequestMapping("bpm-conf-user-remove")
    public String remove(@RequestParam("id") Long id) {
        BpmConfUser bpmConfUser = bpmConfUserManager.findOne(id);
        Long bpmConfNodeId = bpmConfUser.getBpmConfNode().getId();

        if (bpmConfUser.getStatus() == 0) {
            bpmConfUser.setStatus(2);
            bpmConfUserManager.save(bpmConfUser);
        } else if (bpmConfUser.getStatus() == 1) {
            bpmConfUserManager.delete(bpmConfUser);
        } else if (bpmConfUser.getStatus() == 2) {
            bpmConfUser.setStatus(0);
            bpmConfUserManager.save(bpmConfUser);
        }

        return "redirect:/bpm/bpm-conf-user-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }

    // ~ ======================================================================
    @Resource
    public void setBpmConfNodeManager(BpmConfNodeManager bpmConfNodeManager) {
        this.bpmConfNodeManager = bpmConfNodeManager;
    }

    @Resource
    public void setBpmConfUserManager(BpmConfUserManager bpmConfUserManager) {
        this.bpmConfUserManager = bpmConfUserManager;
    }

    @Resource
    public void setBpmConfCountersignManager(
            BpmConfCountersignManager bpmConfCountersignManager) {
        this.bpmConfCountersignManager = bpmConfCountersignManager;
    }
}
