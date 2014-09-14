package com.luna.bpm.web;

import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.Page;
import com.luna.bpm.component.ActivitiProcessConnector;

/**
 * 异步消息管理
 * 
 * @author LuZhao
 * 
 */
@Controller
@RequestMapping("bpm")
public class JobController {
	@Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ActivitiProcessConnector activitiProcessConnector;

    /** 作业列表 */
    @RequestMapping("job-list")
    public String list(@ModelAttribute Page page, Model model) {
        page = activitiProcessConnector.findJobs(page);
        model.addAttribute("page", page);

        return "bpm/job-list";
    }

    /** 执行作业 */
    @RequestMapping("job-executeJob")
    public String executeJob(@RequestParam("id") String id) {
        processEngine.getManagementService().executeJob(id);

        return "redirect:/bpm/job-list.do";
    }

    /** 删除作业 */
    @RequestMapping("job-removeJob")
    public String removeJob(@RequestParam("id") String id) {
        processEngine.getManagementService().deleteJob(id);

        return "redirect:/bpm/job-list.do";
    }
}
