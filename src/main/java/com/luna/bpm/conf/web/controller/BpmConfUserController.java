package com.luna.bpm.conf.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.bpm.conf.service.BpmConfNodeService;
import com.luna.bpm.conf.service.BpmConfUserService;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.service.BpmProcessService;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/user")
public class BpmConfUserController   extends BaseCRUDController<BpmConfUser, Long> {
    private BpmConfUserService getBpmConfUserService() {
        return (BpmConfUserService) baseService;
    }
    
    @Autowired
    private BpmConfNodeService bpmConfNodeService;

    @Autowired
    private BpmProcessService bpmProcessService;
	
	public BpmConfUserController() {
		setResourceIdentity("bpm:conf:user");
    }
	

	@RequestMapping(value = "/process-{processId}/node-{nodeId}", method = RequestMethod.GET)
	@PageableDefaults(sort = "priority=asc")
	public String list(Searchable searchable,
			@PathVariable("processId") Long bpmProcessId, 
			@PathVariable("nodeId") Long bpmConfNodeId, Model model) {
		
		BpmConfNode bpmConfNode = bpmConfNodeService.findOne(bpmConfNodeId);

		BpmProcess bpmProcess = bpmProcessService.findOne(bpmProcessId);
		if(bpmProcess != null){
			model.addAttribute("bpmProcess", bpmProcess);
		}
		
		if (bpmConfNode != null) {
			model.addAttribute("bpmConfNode", bpmConfNode);
			searchable.addSearchFilter("bpmConfNode.id", SearchOperator.eq,
					bpmConfNode.getId());
		}
		return super.list(searchable, model);
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
