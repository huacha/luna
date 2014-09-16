package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.service.BpmConfUserService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/user")
public class BpmConfUserController   extends BaseCRUDController<BpmConfUser, Long> {
    private BpmConfUserService getBpmConfUserService() {
        return (BpmConfUserService) baseService;
    }

	
	public BpmConfUserController() {
		setResourceIdentity("bpm:conf:user");
    }

//    @RequestMapping("bpm-conf-user-list")
//    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
//            Model model) {
//        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
//        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
//        List<BpmConfUser> bpmConfUsers = bpmConfUserManager.findByBpmConfNode(bpmConfNode);
//
//        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
//        model.addAttribute("bpmConfUsers", bpmConfUsers);
//        model.addAttribute("bpmConfCountersign", bpmConfCountersignManager.findByBpmConfNode( bpmConfNode));
//
//        return "bpm/bpm-conf-user-list";
//    }

//    @RequestMapping("bpm-conf-user-save")
//    public String save(@ModelAttribute BpmConfUser bpmConfUser,
//            @RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
//        bpmConfUser.setPriority(0);
//        bpmConfUser.setStatus(1);
//        bpmConfUser.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
//        bpmConfUserManager.save(bpmConfUser);
//
//        return "redirect:/bpm/bpm-conf-user-list.do?bpmConfNodeId="
//                + bpmConfNodeId;
//    }
//
//    @RequestMapping("bpm-conf-user-remove")
//    public String remove(@RequestParam("id") Long id) {
//        BpmConfUser bpmConfUser = bpmConfUserManager.findOne(id);
//        Long bpmConfNodeId = bpmConfUser.getBpmConfNode().getId();
//
//        if (bpmConfUser.getStatus() == 0) {
//            bpmConfUser.setStatus(2);
//            bpmConfUserManager.save(bpmConfUser);
//        } else if (bpmConfUser.getStatus() == 1) {
//            bpmConfUserManager.delete(bpmConfUser);
//        } else if (bpmConfUser.getStatus() == 2) {
//            bpmConfUser.setStatus(0);
//            bpmConfUserManager.save(bpmConfUser);
//        }
//
//        return "redirect:/bpm/bpm-conf-user-list.do?bpmConfNodeId="
//                + bpmConfNodeId;
//    }

}
