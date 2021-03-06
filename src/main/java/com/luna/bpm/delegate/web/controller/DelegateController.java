package com.luna.bpm.delegate.web.controller;

import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.delegate.service.DelegateService;
import com.luna.bpm.process.entity.BpmDelegateHistory;
import com.luna.bpm.process.entity.BpmDelegateInfo;
import com.luna.bpm.process.repository.BpmDelegateHistoryManager;
import com.luna.bpm.process.repository.BpmDelegateInfoManager;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("bpm")
public class DelegateController {
//	private static Logger logger = LoggerFactory.getLogger(DelegateController.class);
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DelegateService delegateService;
	@Autowired
	private BpmDelegateInfoManager bpmDelegateInfoManager;
	@Autowired
	private BpmDelegateHistoryManager bpmDelegateHistoryManager;

	/**
	 * 自动委托列表 TODO 可以指定多个自动委托人？
	 * 
	 * @return
	 */
	@RequestMapping("delegate-listMyDelegateInfos")
	public String listMyDelegateInfos(@CurrentUser User user,Model model) {
		String userId = user.getId().toString();
		List<BpmDelegateInfo> bpmDelegateInfos = bpmDelegateInfoManager
				.findByAssignee(userId);
		model.addAttribute("bpmDelegateInfos", bpmDelegateInfos);

		return "bpm/delegate-listMyDelegateInfos";
	}

	/**
	 * 删除自动委托
	 * 
	 * @return
	 */
	@RequestMapping("delegate-removeDelegateInfo")
	public String removeDelegateInfo(@RequestParam("id") Long id) {
		delegateService.removeRecord(id);

		return "redirect:/bpm/delegate-listMyDelegateInfos.do";
	}

	// ~ ======================================================================
	/**
	 * 自动委托页面
	 * 
	 * @return
	 */
	@RequestMapping("delegate-prepareAutoDelegate")
	public String prepareAutoDelegate(Model model) {
		List<ProcessDefinition> processDefinitions = processEngine
				.getRepositoryService().createProcessDefinitionQuery().list();
		model.addAttribute("processDefinitions", processDefinitions);

		return "bpm/delegate-prepareAutoDelegate";
	}

	/**
	 * 自动委托
	 * 
	 * @return
	 */
	@RequestMapping("delegate-autoDelegate")
	public String autoDelegate(
			@CurrentUser User user,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("attorney") String attorney) throws Exception {
		String userId = user.getId().toString();

		if ((processDefinitionId != null)
				&& "".equals(processDefinitionId.trim())) {
			processDefinitionId = null;
		}

		delegateService.addDelegateInfo(userId, attorney, startTime, endTime,
				processDefinitionId);

		return "redirect:/bpm/delegate-listMyDelegateInfos.do";
	}

	// ~ ======================================================================
	/**
	 * 自动委派
	 */
	@RequestMapping("delegate-listDelegateInfos")
	public String listDelegateInfos(Model model) {
		List<BpmDelegateInfo> bpmDelegateInfos = bpmDelegateInfoManager.findAll();
		model.addAttribute("bpmDelegateInfos", bpmDelegateInfos);

		return "bpm/delegate-listDelegateInfos";
	}

	/**
	 * 自动委托历史
	 * 
	 * @return
	 */
	@RequestMapping("delegate-listDelegateHistories")
	public String listDelegateHistories(Model model) {
		List<BpmDelegateHistory> bpmDelegateHistories = bpmDelegateHistoryManager.findAll();
		model.addAttribute("bpmDelegateHistories", bpmDelegateHistories);

		return "bpm/delegate-listDelegateHistories";
	}

}
