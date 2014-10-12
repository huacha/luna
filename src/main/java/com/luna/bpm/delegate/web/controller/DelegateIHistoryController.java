package com.luna.bpm.delegate.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luna.bpm.delegate.entity.DelegateHistory;
import com.luna.bpm.delegate.service.DelegateHistoryService;
import com.luna.common.Constants;
import com.luna.common.entity.enums.BooleanEnum;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping("/bpm/process/delegate/history")
public class DelegateIHistoryController extends
		BaseCRUDController<DelegateHistory, Long> {
	
	private DelegateHistoryService getDelegateHistoryService() {
		return (DelegateHistoryService) baseService;
	}

	public DelegateIHistoryController() {
		setResourceIdentity("bpm:process:delegate:history");
	}

	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("booleanList", BooleanEnum.values());
	}
}
