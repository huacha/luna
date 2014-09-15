package com.luna.bpm.conf.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.repository.BpmConfFormManager;
import com.luna.bpm.conf.repository.BpmConfNodeManager;

@Controller
@RequestMapping("bpm")
public class BpmConfFormController {
    private BpmConfNodeManager bpmConfNodeManager;
    private BpmConfFormManager bpmConfFormManager;
    @RequestMapping("bpm-conf-form-list")
    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
            Model model) {
        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
        List<BpmConfForm> bpmConfForms = bpmConfFormManager.findByBpmConfNode(bpmConfNode);
        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
        model.addAttribute("bpmConfForms", bpmConfForms);

        return "bpm/bpm-conf-form-list";
    }

    @RequestMapping("bpm-conf-form-save")
    public String save(@ModelAttribute BpmConfForm bpmConfForm,
            @RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
    	BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
        BpmConfForm dest = bpmConfFormManager.findByBpmConfNode(bpmConfNode).get(0);

        if (dest == null) {
            // 如果不存在，就创建一个
            bpmConfForm.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
            bpmConfForm.setStatus(1);
            bpmConfFormManager.save(bpmConfForm);
        } else {
            dest.setValue(bpmConfForm.getValue());
            dest.setType(bpmConfForm.getType());
            dest.setStatus(1);
            bpmConfFormManager.save(dest);
        }

        return "redirect:/bpm/bpm-conf-form-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }

    @RequestMapping("bpm-conf-form-remove")
    public String remove(@RequestParam("id") Long id) {
        BpmConfForm bpmConfForm = bpmConfFormManager.findOne(id);
        Long bpmConfNodeId = bpmConfForm.getBpmConfNode().getId();

        if (bpmConfForm.getStatus() == 0) {
            bpmConfForm.setStatus(2);
            bpmConfFormManager.save(bpmConfForm);
        } else if (bpmConfForm.getStatus() == 2) {
            bpmConfForm.setStatus(1);
            bpmConfFormManager.save(bpmConfForm);
        } else if (bpmConfForm.getStatus() == 1) {
            if (bpmConfForm.getOriginValue() == null) {
                bpmConfFormManager.delete(bpmConfForm);
            } else {
                bpmConfForm.setStatus(0);
                bpmConfForm.setValue(bpmConfForm.getOriginValue());
                bpmConfForm.setType(bpmConfForm.getOriginType());
                bpmConfFormManager.save(bpmConfForm);
            }
        }

        return "redirect:/bpm/bpm-conf-form-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }

    // ~ ======================================================================
    @Resource
    public void setBpmConfNodeManager(BpmConfNodeManager bpmConfNodeManager) {
        this.bpmConfNodeManager = bpmConfNodeManager;
    }

    @Resource
    public void setBpmConfFormManager(BpmConfFormManager bpmConfFormManager) {
        this.bpmConfFormManager = bpmConfFormManager;
    }

}
