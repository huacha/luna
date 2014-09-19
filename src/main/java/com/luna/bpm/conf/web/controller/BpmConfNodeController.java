package com.luna.bpm.conf.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.service.BpmConfBaseService;
import com.luna.bpm.conf.service.BpmConfNodeService;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.service.BpmProcessService;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value = "/bpm/conf/node")
public class BpmConfNodeController extends
		BaseCRUDController<BpmConfNode, Long> {
	private BpmConfNodeService getBpmConfNodeService() {
		return (BpmConfNodeService) baseService;
	}

	@Autowired
	private BpmProcessService bpmProcessService;
	
	@Autowired
	private BpmConfBaseService bpmConfBaseService;
	
	public BpmConfNodeController() {
		setResourceIdentity("bpm:conf:node");
	}

	@RequestMapping(value = "/process-{processId}", method = RequestMethod.GET)
	@PageableDefaults(sort = "priority=asc")
	public String listByProcess(Searchable searchable,
			@PathVariable("processId") Long bpmProcessId, Model model) {
		
		BpmProcess bpmProcess = bpmProcessService.findOne(bpmProcessId);
		if(bpmProcess != null){
			model.addAttribute("bpmProcess", bpmProcess);
		}

		BpmConfBase bpmConfBase =bpmProcess.getBpmConfBase();
		
		if (bpmConfBase != null) {
			model.addAttribute("bpmConfBase", bpmConfBase);
			searchable.addSearchFilter("bpmConfBase.id", SearchOperator.eq,
					bpmConfBase.getId());
		}
		return super.list(searchable, model);
	}
}
