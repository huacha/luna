package com.luna.xform.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.luna.xform.model.ProcessVariable;
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
	
    /**
     * 显示启动流程的表单.
     */
    @RequestMapping("viewStartForm")
    public String viewStartForm(@RequestParam("bpmProcessId") Long bpmProcessId, Model model) throws Exception {
    	Long formid = formProcessService.getStartFormId(bpmProcessId);
    	if(formid == null){
    		return "redirect:/xform/process/start?processId="+bpmProcessId;
    	}
    	FormTemplate m = formTemplateService.findOne(formid);
		model.addAttribute("m", m);
		model.addAttribute("formid", formid);
		model.addAttribute("processId", bpmProcessId);
		logger.debug("找到表单: {}",formid);
		return "xform/process/viewStartForm";
    }
    
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
	public String start(@CurrentUser User user,
			Model model, 
			@RequestParam(value = "formid", required = false) Long formid,
			@RequestParam("processId") Long processId,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		String businessKey = "";
		Map<String,Object> variables = new HashMap<String, Object>();
		List<ProcessVariable> ls = new ArrayList<ProcessVariable>();
		if(formid != null){
			List<FieldModel> fields = formTemplateService.getFields(formid);
			Map<String,Object> map = new HashMap<String, Object>();
			for (FieldModel fieldModel : fields) {
				String key = fieldModel.getName();
				String title = fieldModel.getTitle();
				String val = request.getParameter(key);
				map.put(key, val);
				ProcessVariable pv = new ProcessVariable();
				pv.setCode(key);
				pv.setTitle(title);
				pv.setValue(val);
				ls.add(pv);
			}
			variables.put("开始", ls);
			String insertSql = formTemplateService.getInsertSql(formid);
			long id = dataService.saveAndGetID(insertSql, map);
			businessKey = String.valueOf(id);
		}
		
		BpmProcess process = formProcessService.getProcess(processId);
		String processDefinitionKey = process.getBpmConfBase().getProcessDefinitionKey();
		
		ProcessInstance processInstance = formProcessService.startWorkflow(user,variables,businessKey,processDefinitionKey);
		
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "流程已启动，流程实例ID：" + processInstance.getId());
		
		return "redirect:/xform/process/processInstanceStarted";
	}
	
	@RequestMapping(value = "processInstanceStarted")
	public String processInstanceStarted() {
		return "xform/process/success";
	}
    
	/**
	 * 显示任务表单
	 * 
	 * @param model
	 * @param user
	 * @param taskstatus
	 * @param taskId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("viewTaskForm")
	public String viewTaskForm(Model model, 
			@CurrentUser User user,
			@RequestParam("taskstatus") String taskstatus,
			@RequestParam("taskId") String taskId) throws IOException {
		
		Map<String, Object> variables = taskService.getVariables(taskId);
		logger.info("流程变量：{}",variables);
		Set<String> keys = variables.keySet();
		StringBuilder html = new StringBuilder();
		for (String key : keys) {
			@SuppressWarnings("unchecked")
			List<ProcessVariable> value = (List<ProcessVariable>) variables.get(key);
			html.append(this.htmlProcessVariable(key, value));
		}
		String data = html.toString();
		model.addAttribute("data", data);
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String taskDefinitionKey = task.getTaskDefinitionKey();
		Long formid = formProcessService.getTaskFormId(taskDefinitionKey);
		if(formid != null){
			FormTemplate m = formTemplateService.findOne(formid);
			model.addAttribute("m", m);
			model.addAttribute("formId", formid);
			logger.debug("找到表单: {}",formid);
    	}
		
		String processDefinitionId = task.getProcessDefinitionId();
		String activityId = task.getTaskDefinitionKey();
		List<String> buttons = formProcessService.getButtons(processDefinitionId, taskDefinitionKey);
		model.addAttribute("buttons", buttons);
		model.addAttribute("taskId", taskId);
		model.addAttribute("processDefinitionId", processDefinitionId);
		model.addAttribute("activityId", activityId);
		
		return "xform/process/viewTaskForm";
	}
	
	/**
	 * 组织表单数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private String htmlProcessVariable(String key,List<ProcessVariable> val) {
		StringBuilder sb = new StringBuilder();
		sb.append("<fieldset>");
		sb.append("<legend>").append(key).append("</legend>");
		
		for (ProcessVariable variable : val) {
			String title = variable.getTitle();
			String value = variable.getValue();
			sb.append("<div class=\"control-group\">");
			sb.append("<label class=\"control-label\">");
			sb.append(title);
			sb.append("</label>");
			sb.append("<div class=\"controls\">");
			sb.append("<input type=\"text\" value=\"").append(value).append("\" readonly=\"true\">");
			sb.append("</div>");
			sb.append("</div>");
		}
		sb.append("</fieldset>");
		return sb.toString();
	}
	
	
	@RequestMapping("completeTask")
	public String completeTask(
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		String taskId = request.getParameter("taskId");
		String formId = request.getParameter("formId");
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String taskName = task.getName();
		
		Map<String,Object> variables = new HashMap<String, Object>();
		List<ProcessVariable> ls = new ArrayList<ProcessVariable>();
		if(StringUtils.isNotBlank(formId)){
			Long formid = Long.decode(formId);
			List<FieldModel> fields = formTemplateService.getFields(formid);
			Map<String,Object> map = new HashMap<String, Object>();
			for (FieldModel fieldModel : fields) {
				String key = fieldModel.getName();
				String title = fieldModel.getTitle();
				String val = request.getParameter(key);
				map.put(key, val);
				ProcessVariable pv = new ProcessVariable();
				pv.setCode(key);
				pv.setTitle(title);
				pv.setValue(val);
				ls.add(pv);
			}
			variables.put(taskName, ls);
			String insertSql = formTemplateService.getInsertSql(formid);
			dataService.saveAndGetID(insertSql, map);
		}
		
		taskService.complete(taskId, variables);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "任务已处理，任务名称：" + taskName);
		return "redirect:/xform/process/taskHasCompleted";
	}
	
	@RequestMapping(value = "taskHasCompleted")
	public String success() {
		return "xform/process/success";
	}
	
	@RequestMapping("previous")
	@ResponseBody
	public String previous(String processDefinitionId, String activityId) throws IOException {
		List<PvmActivity> list = formProcessService.getPreviousActivities(processDefinitionId, activityId);
		List<String> names = new ArrayList<String>();
		for (PvmActivity pvmActivity : list) {
			Object obj = pvmActivity.getProperty("name");
			if(obj != null){
				String name = obj.toString();
				names.add(name);
			}
		}
		String json = JsonUtil.toJson(names);
		return json;
	}
	
	@RequestMapping("next")
	@ResponseBody
	public String next(String processDefinitionId, String activityId) throws IOException {
		List<PvmActivity> list = formProcessService.getNextActivities(processDefinitionId, activityId);
		List<String> names = new ArrayList<String>();
		for (PvmActivity pvmActivity : list) {
			Object obj = pvmActivity.getProperty("name");
			if(obj != null){
				String name = obj.toString();
				names.add(name);
			}
		}
		String json = JsonUtil.toJson(names);
		return json;
	}

    
	
    /**
     * 保存草稿.
     */
    @RequestMapping("saveDraft")
    public String saveDraft(@CurrentUser User user,
            @RequestParam MultiValueMap<String, String> multiValueMap)
            throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();

        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
            parameterMap.put(entry.getKey(),entry.getValue().toArray(new String[0]));
        }
//        String userId = user.getId().toString();

        return "form/saveDraft";
    }

    /**
     * 列出所有草稿.
     */
    @RequestMapping("listDrafts")
    public String listDrafts(Model model) throws Exception {
//        String userId = SpringSecurityUtils.getCurrentUserId();
//        List<Record> records = keyValue.findByStatus(STATUS_DRAFT_PROCESS,
//                userId);
//        model.addAttribute("records", records);

        return "form/listDrafts";
    }
}
