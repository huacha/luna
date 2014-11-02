package com.luna.xform.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.bpm.process.entity.BpmProcess;
import com.luna.common.Constants;
import com.luna.common.utils.JsonUtil;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.model.FieldModel;
import com.luna.xform.service.DataService;
import com.luna.xform.service.FormProcessService;
import com.luna.xform.service.FormTemplateService;

@RequestMapping("/xform/process")
@Controller
public class FormProcessController {
	private static Logger logger = LoggerFactory.getLogger(FormProcessController.class);

	@Autowired
	FormProcessService formProcessService;
	@Autowired
	FormTemplateService formTemplateService;
	@Autowired
	DataService dataService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;


	/**
	 * 启动流程
	 * 
	 * @param model
	 * @param formid
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "start")
	public String start(@CurrentUser User user, Model model,
			@RequestParam("processId") Long processId,
			HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws Exception {
		BpmProcess process = formProcessService.getProcess(processId);
		String processDefinitionId = process.getBpmConfBase()
				.getProcessDefinitionId();
		ProcessInstance processInstance = formProcessService.startWorkflow(
				user, processDefinitionId);
		Task firstActiveTask = formProcessService
				.getFirstActiveTask(processInstance);
		formProcessService.setFirstTaskAssignee(user, processDefinitionId,
				firstActiveTask);
		Long taskFormId = formProcessService.getTaskFormId(processDefinitionId,
				firstActiveTask.getTaskDefinitionKey());
		// 如果第一个任务的处理人是发起人自己，且配有表单，则直接导航到viewTaskForm
		if (firstActiveTask.getAssignee().equals(user.getUsername())
				&& taskFormId != null) {
			return "redirect:/xform/process/viewTaskForm?taskId="
					+ firstActiveTask.getId();
		}
		redirectAttributes.addFlashAttribute(Constants.MESSAGE,
				process.getName() + " 流程已启动，流程实例ID：" + processInstance.getId());
		return "redirect:/bpm/userprocess?processstatus=unfinished";
	}

	/**
	 * 显示任务表单
	 * 
	 * @param model
	 * @param user
	 * @param taskstatus
	 * @param taskId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("viewTaskForm")
	public String viewTaskForm(
			Model model,
			@CurrentUser User user,
			@RequestParam(value = "taskstatus", required = false) String taskstatus,
			@RequestParam("taskId") String taskId) throws Exception {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String taskName = task.getName();
		String taskDefinitionKey = task.getTaskDefinitionKey();
		String prcessDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId();
		
		Long formid = formProcessService.getTaskFormId(prcessDefinitionId,
				taskDefinitionKey);
		if (formid != null) {
			FormTemplate m = formTemplateService.findOne(formid);
			model.addAttribute("m", m);
			model.addAttribute("formId", formid);
			logger.debug("找到表单: {}", formid);
		}

		String processDefinitionId = task.getProcessDefinitionId();
		String activityId = task.getTaskDefinitionKey();
		List<String> buttons = formProcessService.getButtons(processDefinitionId, taskDefinitionKey);
		
		model.addAttribute("buttons", buttons);
		model.addAttribute("taskId", taskId);
		model.addAttribute("taskName", taskName);
		model.addAttribute("processDefinitionId", processDefinitionId);
		model.addAttribute("activityId", activityId);

		List<HistoricTaskInstance> list = historyService
				.createHistoricTaskInstanceQuery().finished()
				.processInstanceId(processInstanceId)
				.orderByHistoricTaskInstanceEndTime().asc().list();
		StringBuilder html = new StringBuilder();
		for (HistoricTaskInstance his : list) {
			String name = his.getName();
			Map<String, Object> taskBussinessData = dataService.findTaskBussinessData(his.getId());
			Map<String, String> transData = dataService.translateTaskBussinessData(processDefinitionId, taskDefinitionKey, taskBussinessData);
			html.append(this.orgTaskData(name,transData));
		}
		
		String data = html.toString();
		model.addAttribute("data", data);

		return "xform/process/viewTaskForm";
	}

	/**
	 * 组织表单数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private String orgTaskData(String taskName,Map<String, String> taskBussinessData) {
		StringBuilder sb = new StringBuilder();
		sb.append("<fieldset>");
		sb.append("<legend>").append(taskName).append("</legend>");

		if (taskBussinessData != null) {
			Set<String> keySet = taskBussinessData.keySet();
			for (String key : keySet) {
				String title = key;
				String value = taskBussinessData.get(key);
				sb.append("<div class=\"control-group\">");
				sb.append("<label class=\"control-label\">");
				sb.append(title);
				sb.append("</label>");
				sb.append("<div class=\"controls\">");
				sb.append("<input type=\"text\" value=\"").append(value).append("\" readonly=\"true\">");
				sb.append("</div>");
				sb.append("</div>");
			}
		}
		sb.append("</fieldset>");
		return sb.toString();
	}

	/**
	 * 任务处理
	 * 
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("completeTask")
	public String completeTask(HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		String taskId = request.getParameter("taskId");
		String taskName = request.getParameter("taskName");
		String formId = request.getParameter("formId");

		Map<String, Object> taskVariables = new HashMap<String, Object>();// 任务流程变量
		if (StringUtils.isNotBlank(formId)) {
			Long formid = Long.decode(formId);
			List<FieldModel> fields = formTemplateService.getFields(formid);
			Map<String, Object> map = new HashMap<String, Object>();
			for (FieldModel fieldModel : fields) {
				String key = fieldModel.getName();
				String val = request.getParameter(key);
				map.put(key, val);
				taskVariables.put(key, val);
			}
			String insertSql = formTemplateService.getInsertSql(formid);
			long bussid = dataService.saveAndGetID(insertSql, map);
			FormTemplate formTemplate = formTemplateService.getFormTemplate(formid);
			String tableName = formTemplate.getCode();
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			dataService.saveTaskBussiness(processInstanceId, taskId, taskName, tableName, bussid);
		}
		taskService.setVariablesLocal(taskId, taskVariables);
		taskService.complete(taskId);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "任务已处理，任务名称：" + taskName);
		return "redirect:/bpm/userprocess?processstatus=unfinished";
	}

	/**
	 * 回退任务
	 * 
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("rollbackTask")
	public String rollbackTask(HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		String taskId = request.getParameter("taskId");
		String taskName = request.getParameter("taskName");
		String formId = request.getParameter("formId");

		Map<String, Object> taskVariables = new HashMap<String, Object>();// 任务流程变量
		if (StringUtils.isNotBlank(formId)) {
			Long formid = Long.decode(formId);
			List<FieldModel> fields = formTemplateService.getFields(formid);
			Map<String, Object> map = new HashMap<String, Object>();
			for (FieldModel fieldModel : fields) {
				String key = fieldModel.getName();
				String val = request.getParameter(key);
				map.put(key, val);
				taskVariables.put(key, val);
			}
			String insertSql = formTemplateService.getInsertSql(formid);
			long bussid = dataService.saveAndGetID(insertSql, map);
			FormTemplate formTemplate = formTemplateService.getFormTemplate(formid);
			String tableName = formTemplate.getCode();
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			dataService.saveTaskBussiness(processInstanceId, taskId, taskName, tableName, bussid);
		}
		taskService.setVariablesLocal(taskId, taskVariables);

		Integer rollbackTask = formProcessService.rollbackTask(taskId);
		logger.debug(rollbackTask.toString());

		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "任务已回退，任务名称："
				+ taskName);
		return "redirect:/bpm/userprocess?processstatus=unfinished";
	}
	
	/**
	 * 转办请求.
	 */
	@RequestMapping(value="doDelegate", method = RequestMethod.GET)
	public String doDelegateView(@RequestParam("taskId") String taskId,
			@CurrentUser User user,
			org.springframework.ui.Model model) {
		model.addAttribute("taskId", taskId);
		model.addAttribute("m", new User());
		
		//formProcessService.doDelegate(taskId, attorney, user);

		return "xform/process/delegateForm";
	}
	
	/**
	 * 转办.
	 */
	@RequestMapping(value="doDelegate", method = RequestMethod.POST)
	public String doDelegate(@RequestParam("taskId") String taskId,
			@RequestParam("username") String attorney, @CurrentUser User user) {
		
		formProcessService.doDelegate(taskId, attorney, user);

		return "redirect:/bpm/usertask?taskstatus=prepare";
	}
	
	/**
	 * 协办请求.
	 */
	@RequestMapping(value="doDelegateHelp", method = RequestMethod.GET)
	public String doDelegateHelpView(@RequestParam("taskId") String taskId,
			@CurrentUser User user,
			org.springframework.ui.Model model) {
		model.addAttribute("taskId", taskId);
		model.addAttribute("m", new User());
		return "xform/process/delegateForm";
	}

	/**
	 * 协办.
	 */
	@RequestMapping("doDelegateHelp")
	public String doDelegateHelp(@RequestParam("taskId") String taskId,
			@RequestParam("username") String attorney) {
		taskService.delegateTask(taskId, attorney);
		return "redirect:/bpm/usertask?taskstatus=prepare";
	}
	
	/**
	 * 查询上一环节
	 * 
	 * @param processDefinitionId
	 * @param activityId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("previous")
	@ResponseBody
	public String previous(String processDefinitionId, String activityId)
			throws IOException {
		List<PvmActivity> list = formProcessService.getPreviousActivities(
				processDefinitionId, activityId);
		List<String> names = new ArrayList<String>();
		for (PvmActivity pvmActivity : list) {
			Object obj = pvmActivity.getProperty("name");
			if (obj != null) {
				String name = obj.toString();
				names.add(name);
			}
		}
		String json = JsonUtil.toJson(names);
		return json;
	}

	/**
	 * 查询下一环节
	 * 
	 * @param processDefinitionId
	 * @param activityId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("next")
	@ResponseBody
	public String next(String processDefinitionId, String activityId)
			throws IOException {
		List<PvmActivity> list = formProcessService.getNextActivities(
				processDefinitionId, activityId);
		List<String> names = new ArrayList<String>();
		for (PvmActivity pvmActivity : list) {
			Object obj = pvmActivity.getProperty("name");
			if (obj != null) {
				String name = obj.toString();
				names.add(name);
			}
		}
		String json = JsonUtil.toJson(names);
		return json;
	}

}
