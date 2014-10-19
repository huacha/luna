package com.luna.xform.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.bpm.process.entity.BpmProcess;
import com.luna.common.Constants;
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
		if(formid != null){
			List<FieldModel> fields = formTemplateService.getFields(formid);
			Map<String,Object> map = new HashMap<String, Object>();
			for (FieldModel fieldModel : fields) {
				String key = fieldModel.getName();
				String title = fieldModel.getTitle();
				String val = request.getParameter(key);
				map.put(key, val);
				variables.put(title, val);
			}
			String insertSql = formTemplateService.getInsertSql(formid);
			long id = dataService.saveAndGetID(insertSql, map);
			businessKey = String.valueOf(id);
		}
		
		BpmProcess process = formProcessService.getProcess(processId);
		String processDefinitionKey = process.getBpmConfBase().getProcessDefinitionKey();
		
		ProcessInstance processInstance = formProcessService.startWorkflow(user,variables,businessKey,processDefinitionKey);
		
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "流程已启动，流程ID：" + processInstance.getId());
		
		return "redirect:/xform/process/startProcessInstance";
	}
	
	@RequestMapping(value = "startProcessInstance")
	public String startProcessInstance() {
		return "xform/process/startProcessInstance";
	}
    
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
			String value = variables.get(key).toString();
			html.append(this.html(key, value));
		}
		String data = html.toString();
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String taskDefinitionKey = task.getTaskDefinitionKey();
		Long formid = formProcessService.getTaskFormId(taskDefinitionKey);
		if(formid == null){
    		return "redirect:/xform/process/start?taskId="+taskId;
    	}
    	FormTemplate m = formTemplateService.findOne(formid);
		model.addAttribute("m", m);
		model.addAttribute("formid", formid);
		model.addAttribute("taskId", taskId);
		model.addAttribute("data", data);
		logger.debug("找到表单: {}",formid);
		return "xform/process/viewTaskForm";
	}
	
	private String html(String key,String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"control-group\">");
		sb.append("<label class=\"control-label\">");
		sb.append(key);
		sb.append("</label>");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" value=\"").append(value).append("\" readonly=\"true\">");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
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
