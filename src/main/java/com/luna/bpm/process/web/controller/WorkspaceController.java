package com.luna.bpm.process.web.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.category.repository.BpmCategoryManager;
import com.luna.bpm.conf.service.BpmConfBaseService;
import com.luna.bpm.process.ActivitiProcessConnector;
import com.luna.bpm.process.cmd.CounterSignCmd;
import com.luna.bpm.process.cmd.HistoryProcessInstanceDiagramCmd;
import com.luna.bpm.process.cmd.ProcessDefinitionDiagramCmd;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.service.BpmProcessService;

/**
 * 我的流程 待办流程 已办未结
 */
@Controller
@RequestMapping("bpm/process/workspace")
public class WorkspaceController {
	@Autowired
    private BpmCategoryManager bpmCategoryManager;
	@Autowired
    private BpmProcessService bpmProcessService;
	@Autowired
    private BpmConfBaseService bpmConfBaseService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ActivitiProcessConnector activitiProcessConnector;

    @RequestMapping("home")
    public String home(Model model) {
        List<BpmCategory> bpmCategories = bpmCategoryManager.findAll(new Sort(new String[]{"priority"}));
        model.addAttribute("bpmCategories", bpmCategories);
        return "bpm/process/workspace/home";
    }

    @RequestMapping("graphProcessDefinition")
    public void graphProcessDefinition(
            @RequestParam("bpmProcessId") Long bpmProcessId,
            HttpServletResponse response) throws Exception {
        BpmProcess bpmProcess = bpmProcessService.findOne(bpmProcessId);
        String processDefinitionId = bpmProcess.getBpmConfBase().getProcessDefinitionId();

        Command<InputStream> cmd = new ProcessDefinitionDiagramCmd(processDefinitionId);
        InputStream is = processEngine.getManagementService().executeCommand(cmd);
        response.setContentType("image/png");

        IOUtils.copy(is, response.getOutputStream());
    }

//    @RequestMapping("endProcessInstance")
//    public String endProcessInstance(
//            @RequestParam("processInstanceId") String processInstanceId) {
//        processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, "end");
//        return "redirect:/bpm/process/workspace/listProcessInstances";
//    }


    /**
     * 流程跟踪
     * 
     * @throws Exception
     */
    @RequestMapping("graphHistoryProcessInstance")
    public void graphHistoryProcessInstance(
            @RequestParam("processInstanceId") String processInstanceId,
            HttpServletResponse response) throws Exception {
        Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                processInstanceId);

        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        response.setContentType("image/png");

        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * TODO 该方法有用到？
     * 
     * @return
     */
    @RequestMapping("resolveTask")
    public String resolveTask(@RequestParam("taskId") String taskId) {
        TaskService taskService = processEngine.getTaskService();
        taskService.resolveTask(taskId);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }

    /**
     * 查看历史【包含流程跟踪、任务列表（完成和未完成）、流程变量】
     * 
     * @return
     */
    @RequestMapping("viewHistory")
    public String viewHistory(
            @RequestParam("processInstanceId") String processInstanceId,
            Model model) {
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcess = historyService
        		.createHistoricProcessInstanceQuery()
        		.processInstanceId(processInstanceId).singleResult();
        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
        BpmProcess bpmProcess = bpmProcessService
        		.findByBpmConfBase(bpmConfBaseService
        				.findByProcessDefinitionId(historicProcess.getProcessDefinitionId()));
        model.addAttribute("bpmProcess", bpmProcess);
        model.addAttribute("historicProcess", historicProcess);
        model.addAttribute("historicTasks", historicTasks);
        model.addAttribute("historicVariableInstances",
                historicVariableInstances);

        return "bpm/process/workspace/viewHistory";
    }

    // ~ ==================================国内特色流程====================================
    /**
     * 准备加减签.
     */
    @RequestMapping("changeCounterSign")
    public String changeCounterSign() {
        return "bpm/process/workspace/changeCounterSign";
    }

    /**
     * 进行加减签.
     */
    @RequestMapping("saveCounterSign")
    public String saveCounterSign(
            @RequestParam("operationType") String operationType,
            @RequestParam("userId") String userId,
            @RequestParam("taskId") String taskId) {
        CounterSignCmd cmd = new CounterSignCmd(operationType, userId, taskId);

        processEngine.getManagementService().executeCommand(cmd);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }
}
