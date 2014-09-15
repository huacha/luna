package com.luna.bpm.conf.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfCountersign;
import com.luna.bpm.conf.repository.BpmConfCountersignManager;
import com.luna.common.utils.BeanMapper;

@Controller
@RequestMapping("bpm")
public class BpmConfCountersignController {
    private BeanMapper beanMapper = new BeanMapper();
    @Autowired
    private BpmConfCountersignManager bpmConfCountersignManager;

    @RequestMapping("bpm-conf-countersign-save")
    public String save(@ModelAttribute BpmConfCountersign bpmConfCountersign,@RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
    	
        BpmConfCountersign dest = bpmConfCountersignManager.findOne(bpmConfCountersign.getId());
        beanMapper.copy(bpmConfCountersign, dest);
        bpmConfCountersignManager.save(dest);

        return "redirect:/bpm/bpm-conf-user-list.do?bpmConfNodeId="
                + bpmConfNodeId;
    }
}
