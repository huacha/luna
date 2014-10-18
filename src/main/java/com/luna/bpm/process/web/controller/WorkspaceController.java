package com.luna.bpm.process.web.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luna.bpm.Page;
import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.category.repository.BpmCategoryManager;
import com.luna.bpm.process.ActivitiProcessConnector;
import com.luna.bpm.process.cmd.CounterSignCmd;
import com.luna.bpm.process.cmd.DelegateTaskCmd;
import com.luna.bpm.process.cmd.HistoryProcessInstanceDiagramCmd;
import com.luna.bpm.process.cmd.ProcessDefinitionDiagramCmd;
import com.luna.bpm.process.cmd.RollbackTaskCmd;
import com.luna.bpm.process.cmd.WithdrawTaskCmd;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.repository.BpmProcessManager;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

/**
 * 我的流程 待办流程 已办未结
 */
@Controller
@RequestMapping("bpm/process/workspace")
public class WorkspaceController {
	@Autowired
    private BpmCategoryManager bpmCategoryManager;
	@Autowired
    private BpmProcessManager bpmProcessManager;
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
        BpmProcess bpmProcess = bpmProcessManager.getOne(bpmProcessId);
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
     * 流程列表（所有的流程定义即流程模型）
     * //TODO 未使用方法？
     * @return
     */
    @RequestMapping("listProcessDefinitions")
    public String listProcessDefinitions(Model model) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().active().list();
        model.addAttribute("processDefinitions", processDefinitions);

        return "bpm/process/workspace/listProcessDefinitions";
    }

    @RequestMapping("listRunningProcessInstances")
    public String listRunningProcessInstances(@CurrentUser User user,@ModelAttribute Page page,
            Model model) {
    	
    	//使用自定义的注解@CurrentUser，可以获取到当前用户！
    	String userId = user.getId().toString();

        page = activitiProcessConnector.findRunningProcessInstances(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listRunningProcessInstances";
    }

    /**
     * 已结流程.
     * 
     * @return
     */
    @RequestMapping("listCompletedProcessInstances")
    public String listCompletedProcessInstances(@CurrentUser User user,@ModelAttribute Page page,
            Model model) {
    	String userId = user.getId().toString();
        page = activitiProcessConnector.findCompletedProcessInstances(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listCompletedProcessInstances";
    }

    /**
     * 用户参与的流程（包含已经完成和未完成）
     * 
     * @return
     */
    @RequestMapping("listInvolvedProcessInstances")
    public String listInvolvedProcessInstances(@CurrentUser User user,@ModelAttribute Page page,
            Model model) {
        // TODO: finished(), unfinished()
    	String userId = user.getId().toString();
    	page = activitiProcessConnector.findInvolvedProcessInstances(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listInvolvedProcessInstances";
    }

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
     * 待办任务（个人任务）
     * 
     * @return
     */
    @RequestMapping("listPersonalTasks")
    public String listPersonalTasks(@CurrentUser User user,@ModelAttribute Page page, Model model) {
    	String userId = user.getId().toString();
    	page = activitiProcessConnector.findPersonalTasks(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listPersonalTasks";
    }

    /**
     * 代领任务（组任务）
     * 
     * @return
     */
    @RequestMapping("listGroupTasks")
    public String listGroupTasks(@CurrentUser User user,@ModelAttribute Page page, Model model) {
    	String userId = user.getId().toString();
        page = activitiProcessConnector.findGroupTasks(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listGroupTasks";
    }

    /**
     * 已办任务（历史任务）
     * 
     * @return
     */
    @RequestMapping("listHistoryTasks")
    public String listHistoryTasks(@CurrentUser User user,@ModelAttribute Page page, Model model) {
    	String userId = user.getId().toString();
        page = activitiProcessConnector.findHistoryTasks(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listHistoryTasks";
    }

    /**
     * 代理中的任务（代理人还未完成该任务）
     * 
     * @return
     */
    @RequestMapping("listDelegatedTasks")
    public String listDelegatedTasks(@CurrentUser User user,@ModelAttribute Page page, Model model) {
    	String userId = user.getId().toString();
        page = activitiProcessConnector.findGroupTasks(userId, page);
        model.addAttribute("page", page);

        return "bpm/process/workspace/listDelegatedTasks";
    }

    // ~ ======================================================================
    /**
     * 发起流程页面（启动一个流程实例）内置流程表单方式
     * 
     * @return
     */
    @RequestMapping("prepareStartProcessInstance")
    public String prepareStartProcessInstance(
            @RequestParam("processDefinitionId") String processDefinitionId,
            Model model) {
        FormService formService = processEngine.getFormService();
        StartFormData startFormData = formService
                .getStartFormData(processDefinitionId);
        model.addAttribute("startFormData", startFormData);

        return "bpm/process/workspace/prepareStartProcessInstance";
    }

    // ~ ======================================================================
    /**
     * 完成任务页面
     * 
     * @return
     */
    @RequestMapping("prepareCompleteTask")
    public String prepareCompleteTask(@RequestParam("taskId") String taskId,
            Model model) {
        FormService formService = processEngine.getFormService();

        TaskFormData taskFormData = formService.getTaskFormData(taskId);

        model.addAttribute("taskFormData", taskFormData);

        return "bpm/process/workspace/prepareCompleteTask";
    }

    /**
     * 认领任务（对应的是在组任务，即从组任务中领取任务）
     * 
     * @return
     */
    @RequestMapping("claimTask")
    public String claimTask(@CurrentUser User user,@RequestParam("taskId") String taskId) {
    	String userId = user.getId().toString();
        TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }

    /**
     * 任务代理页面
     * 
     * @return
     */
    @RequestMapping("prepareDelegateTask")
    public String prepareDelegateTask() {
        return "bpm/process/workspace/prepareDelegateTask";
    }

    /**
     * 任务代理
     * 
     * @return
     */
    @RequestMapping("delegateTask")
    public String delegateTask(@RequestParam("taskId") String taskId,
            @RequestParam("userId") String userId) {
        TaskService taskService = processEngine.getTaskService();
        taskService.delegateTask(taskId, userId);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
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
        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
        model.addAttribute("historicTasks", historicTasks);
        model.addAttribute("historicVariableInstances",
                historicVariableInstances);

        return "bpm/process/workspace/viewHistory";
    }

    // ~ ==================================国内特色流程====================================
    /**
     * 回退任务
     * 
     * @return
     */
    @RequestMapping("rollback")
    public String rollback(@RequestParam("taskId") String taskId) {
        Command<Integer> cmd = new RollbackTaskCmd(taskId);

        processEngine.getManagementService().executeCommand(cmd);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }

    /**
     * 取回任务
     * 
     * @return
     */
    @RequestMapping("withdraw")
    public String withdraw(@RequestParam("taskId") String taskId) {
        Command<Integer> cmd = new WithdrawTaskCmd(taskId);

        processEngine.getManagementService().executeCommand(cmd);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }

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

    /**
     * 转办.
     */
    @RequestMapping("doDelegate")
    public String doDelegate(@RequestParam("taskId") String taskId,
            @RequestParam("attorney") String attorney) {
        DelegateTaskCmd cmd = new DelegateTaskCmd(taskId, attorney);
        processEngine.getManagementService().executeCommand(cmd);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }

    /**
     * 协办.
     */
    @RequestMapping("doDelegateHelp")
    public String doDelegateHelp(@RequestParam("taskId") String taskId,
            @RequestParam("attorney") String attorney) {
        TaskService taskService = processEngine.getTaskService();
        taskService.delegateTask(taskId, attorney);

        return "redirect:/bpm/process/workspace/listPersonalTasks";
    }

}
