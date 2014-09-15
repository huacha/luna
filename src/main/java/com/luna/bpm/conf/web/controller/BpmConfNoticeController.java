package com.luna.bpm.conf.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfNotice;
import com.luna.bpm.conf.repository.BpmConfNodeManager;
import com.luna.bpm.conf.repository.BpmConfNoticeManager;
import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.bpm.process.repository.BpmMailTemplateManager;

@Controller
@RequestMapping("bpm")
public class BpmConfNoticeController {
	private BpmConfNodeManager bpmConfNodeManager;
	private BpmConfNoticeManager bpmConfNoticeManager;
	private BpmMailTemplateManager bpmMailTemplateManager;

	@RequestMapping("bpm-conf-notice-list")
	public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
			Model model) {
		BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
		Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
		List<BpmConfNotice> bpmConfNotices = bpmConfNoticeManager
				.findByBpmConfNode(bpmConfNode);
		List<BpmMailTemplate> bpmMailTemplates = bpmMailTemplateManager
				.findAll();

		model.addAttribute("bpmConfBaseId", bpmConfBaseId);
		model.addAttribute("bpmConfNotices", bpmConfNotices);
		model.addAttribute("bpmMailTemplates", bpmMailTemplates);

		return "bpm/bpm-conf-notice-list";
	}

	@RequestMapping("bpm-conf-notice-input")
	public String input(Model model) {
		List<BpmMailTemplate> bpmMailTemplates = bpmMailTemplateManager
				.findAll();
		model.addAttribute("bpmMailTemplates", bpmMailTemplates);

		return "bpm/bpm-conf-notice-input";
	}

	@RequestMapping("bpm-conf-notice-save")
	public String save(@ModelAttribute BpmConfNotice bpmConfNotice,
			@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
			@RequestParam("bpmMailTemplateId") Long bpmMailTemplateId) {
		bpmConfNotice.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
		bpmConfNotice.setBpmMailTemplate(bpmMailTemplateManager.findOne(bpmMailTemplateId));
		bpmConfNoticeManager.save(bpmConfNotice);

		return "redirect:/bpm/bpm-conf-notice-list.do?bpmConfNodeId="
				+ bpmConfNodeId;
	}

	@RequestMapping("bpm-conf-notice-remove")
	public String remove(@RequestParam("id") Long id) {
		BpmConfNotice bpmConfNotice = bpmConfNoticeManager.findOne(id);
		Long bpmConfNodeId = bpmConfNotice.getBpmConfNode().getId();
		bpmConfNoticeManager.delete(bpmConfNotice);

		return "redirect:/bpm/bpm-conf-notice-list.do?bpmConfNodeId="
				+ bpmConfNodeId;
	}

	// ~ ======================================================================
	@Resource
	public void setBpmConfNodeManager(BpmConfNodeManager bpmConfNodeManager) {
		this.bpmConfNodeManager = bpmConfNodeManager;
	}

	@Resource
	public void setBpmConfNoticeManager(
			BpmConfNoticeManager bpmConfNoticeManager) {
		this.bpmConfNoticeManager = bpmConfNoticeManager;
	}

	@Resource
	public void setBpmMailTemplateManager(
			BpmMailTemplateManager bpmMailTemplateManager) {
		this.bpmMailTemplateManager = bpmMailTemplateManager;
	}

}
