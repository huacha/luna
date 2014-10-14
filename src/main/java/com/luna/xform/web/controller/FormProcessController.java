package com.luna.xform.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    /**
     * 显示启动流程的表单.
     */
    @RequestMapping("viewStartForm")
    public String viewStartForm(@RequestParam("bpmProcessId") Long bpmProcessId, Model model) throws Exception {
    	Long formid = formProcessService.getFormId(bpmProcessId);
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
	@RequestMapping(value = "{formid}/start/{processId}", method = RequestMethod.POST)
	public String start(@CurrentUser User user,
			Model model, 
			@PathVariable("formid") Long formid,
			@PathVariable("processId") Long processId,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		
		List<FieldModel> fields = formTemplateService.getFields(formid);
		Map<String,Object> map = new HashMap<String, Object>();
		for (FieldModel fieldModel : fields) {
			String key = fieldModel.getName();
			String val = request.getParameter(key);
			map.put(key, val);
		}
		String insertSql = formTemplateService.getInsertSql(formid);
		dataService.save(insertSql, map);
		
		String querySql = formTemplateService.getSelectSql(formid, map);
		List<Map<String, Object>> list = dataService.findAll(querySql);
		
		if(list.size() != 1){
			throw new RuntimeException("数据不正确！");
		}
		String businessKey = list.get(0).get("ID").toString();
		
		BpmProcess process = formProcessService.getProcess(processId);
		String processDefinitionKey = process.getBpmConfBase().getProcessDefinitionKey();
		
		ProcessInstance processInstance = formProcessService.startWorkflow(user, map, businessKey,processDefinitionKey);
		
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "流程已启动，流程ID：" + processInstance.getId());
		
		return "xform/process/startProcessInstance";
	}
	
//	@RequestMapping(value = "{formid}/{dataid}/update", method = RequestMethod.GET)
//	public String showUpdateForm(Model model, @PathVariable("formid") Long formid, @PathVariable("dataid") Long dataid) throws IOException {
//		FormTemplate m = formTemplateService.findOne(formid);
//		model.addAttribute("m", m);
//		model.addAttribute("formid", formid);
//		model.addAttribute("dataid", dataid);
//		String sql = formTemplateService.getSelectSql(formid,dataid);
//		Map<String, Object> data = dataService.findOne(sql);
//		String json = JsonUtil.toJson(data);
//		model.addAttribute("json", json);
//		return "xform/render/editForm";
//	}
//	
//	@RequestMapping(value = "{formid}/{dataid}/update", method = RequestMethod.POST)
//	public String update(Model model, @PathVariable("formid") Long formid, @PathVariable("dataid") Long dataid,HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
//		List<FieldModel> fields = formTemplateService.getFields(formid);
//		Map<String,String> map = new HashMap<String, String>();
//		for (FieldModel fieldModel : fields) {
//			String key = fieldModel.getName();
//			String val = request.getParameter(key);
//			map.put(key, val);
//		}
//		map.put("id", String.valueOf(dataid));
//		String sql = formTemplateService.getUpdateSql(formid);
//		int count = dataService.update(sql, map);
//		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "成功修改: "+count);
//		return "redirect:/xform/render/"+formid;
//	}
//
//	@RequestMapping(value = "{formid}/batch/delete", method = { RequestMethod.GET,RequestMethod.POST })
//	public String deleteInBatch(@PathVariable("formid") Long formid, @RequestParam(value = "ids", required = false) Long[] ids,RedirectAttributes redirectAttributes) {
//		String sql = formTemplateService.getDeleteSql(formid,ids);
//		int count = dataService.delete(sql);
//		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "成功删除: "+count);
//		return "redirect:/xform/render/"+formid;
//	}

//    /**
//     * 配置每个任务的参与人.
//     */
//    @RequestMapping("taskConf")
//    public String taskConf(
//            @RequestParam MultiValueMap<String, String> multiValueMap,
//            @RequestParam("bpmProcessId") Long bpmProcessId,
//            @RequestParam(value = "businessKey", required = false) String businessKey,
//            @RequestParam(value = "nextStep", required = false) String nextStep,
//            Model model) {
//        model.addAttribute("bpmProcessId", bpmProcessId);
//
//        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
//
//        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
//            parameterMap.put(entry.getKey(),
//                    entry.getValue().toArray(new String[0]));
//        }
//
//        businessKey = new SaveDraftOperation().execute(parameterMap);
//        model.addAttribute("businessKey", businessKey);
//
//        BpmProcess bpmProcess = bpmProcessManager.getOne(bpmProcessId);
//        String processDefinitionId = bpmProcess.getBpmConfBase()
//                .getProcessDefinitionId();
//
//        if (Integer.valueOf(1).equals(bpmProcess.getUseTaskConf())) {
//            // 如果需要配置负责人
//            nextStep = "confirmStartProcess";
//            model.addAttribute("nextStep", nextStep);
//
//            FindTaskDefinitionsCmd cmd = new FindTaskDefinitionsCmd(
//                    processDefinitionId);
//            List<TaskDefinition> taskDefinitions = processEngine
//                    .getManagementService().executeCommand(cmd);
//            model.addAttribute("taskDefinitions", taskDefinitions);
//
//            return "form/taskConf";
//        } else {
//            // 如果不需要配置负责人，就进入确认发起流程的页面
//            return confirmStartProcess(bpmProcessId, multiValueMap, model);
//        }
//    }
//
//    @RequestMapping("confirmStartProcess")
//    public String confirmStartProcess(
//            @RequestParam("bpmProcessId") Long bpmProcessId,
//            @RequestParam MultiValueMap<String, String> multiValueMap,
//            Model model) {
//        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
//
//        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
//            parameterMap.put(entry.getKey(),
//                    entry.getValue().toArray(new String[0]));
//        }
//
//        String businessKey = new ConfAssigneeOperation().execute(parameterMap);
//        String nextStep = "startProcessInstance";
//        model.addAttribute("businessKey", businessKey);
//        model.addAttribute("nextStep", nextStep);
//        model.addAttribute("bpmProcessId", bpmProcessId);
//
//        return "form/confirmStartProcess";
//    }
//
//    /**
//     * 发起流程.
//     */
//    @RequestMapping("startProcessInstance")
//    public String startProcessInstance(
//            @RequestParam MultiValueMap<String, String> multiValueMap,
//            Model model) throws Exception {
//        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
//
//        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
//            parameterMap.put(entry.getKey(),
//                    entry.getValue().toArray(new String[0]));
//        }
//
//        new StartProcessOperation().execute(parameterMap);
//
//        return "form/startProcessInstance";
//    }
//
//    /**
//     * 工具方法，获取表单的类型.
//     */
//    private String getFormType(Map<String, String> formTypeMap, String name) {
//        if (formTypeMap.containsKey(name)) {
//            return formTypeMap.get(name);
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 显示任务表单.
//     */
//    @RequestMapping("viewTaskForm")
//    public String viewTaskForm(@RequestParam("taskId") String taskId,
//            Model model, RedirectAttributes redirectAttributes)
//            throws Exception {
//        TaskService taskService = processEngine.getTaskService();
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//
//        if (task == null) {
//            messageHelper.addFlashMessage(redirectAttributes, "任务不存在");
//
//            return "redirect:/bpm/workspace-listPersonalTasks.do";
//        }
//
//        FormService formService = processEngine.getFormService();
//        String taskFormKey = formService.getTaskFormKey(
//                task.getProcessDefinitionId(), task.getTaskDefinitionKey());
//
//        FormTemplate formTemplate = formTemplateManager.findUniqueBy("code",
//                taskFormKey);
//        model.addAttribute("formTemplate", formTemplate);
//
//        FormInfo formInfo = new FormInfo();
//        formInfo.setTaskId(taskId);
//        model.addAttribute("formInfo", formInfo);
//
//        List<BpmConfOperation> bpmConfOperations = bpmConfOperationManager
//                .find("from BpmConfOperation where bpmConfNode.bpmConfBase.processDefinitionId=? and bpmConfNode.code=?",
//                        task.getProcessDefinitionId(),
//                        task.getTaskDefinitionKey());
//
//        for (BpmConfOperation bpmConfOperation : bpmConfOperations) {
//            formInfo.getButtons().add(bpmConfOperation.getValue());
//        }
//
//        String processDefinitionId = task.getProcessDefinitionId();
//        String activityId = task.getTaskDefinitionKey();
//        formInfo.setProcessDefinitionId(processDefinitionId);
//        formInfo.setActivityId(activityId);
//
//        List<BpmConfForm> bpmConfForms = bpmConfFormManager
//                .find("from BpmConfForm where bpmConfNode.bpmConfBase.processDefinitionId=? and bpmConfNode.code=?",
//                        processDefinitionId, activityId);
//
//        if (!bpmConfForms.isEmpty()) {
//            if (Integer.valueOf(1).equals(bpmConfForms.get(0).getType())) {
//                String redirectUrl = bpmConfForms.get(0).getValue()
//                        + "?taskId=" + taskId;
//
//                return "redirect:" + redirectUrl;
//            }
//        }
//
//        if ((formTemplate != null)
//                && Integer.valueOf(1).equals(formTemplate.getType())) {
//            String redirectUrl = formTemplate.getContent() + "?taskId="
//                    + taskId;
//
//            return "redirect:" + redirectUrl;
//        }
//
//        if ((taskId != null) && (!"".equals(taskId))) {
//            // 如果是任务草稿，直接通过processInstanceId获得record，更新数据
//            // TODO: 分支肯定有问题
//            String processInstanceId = task.getProcessInstanceId();
//            Record record = keyValue.findByRef(processInstanceId);
//
//            if (record != null) {
//                Map map = new HashMap();
//
//                for (Prop prop : record.getProps().values()) {
//                    map.put(prop.getCode(), prop.getValue());
//                }
//
//                String json = jsonMapper.toJson(map);
//                model.addAttribute("json", json);
//            }
//        }
//
//        return "form/viewTaskForm";
//    }
//
//    /**
//     * 完成任务.
//     */
//    @RequestMapping("completeTask")
//    public String completeTask(
//            @RequestParam MultiValueMap<String, String> multiValueMap,
//            RedirectAttributes redirectAttributes) throws Exception {
//        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
//
//        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
//            parameterMap.put(entry.getKey(),
//                    entry.getValue().toArray(new String[0]));
//        }
//
//        try {
//            new CompleteTaskOperation().execute(parameterMap);
//        } catch (IllegalStateException ex) {
//            logger.error(ex.getMessage(), ex);
//            messageHelper.addFlashMessage(redirectAttributes, ex.getMessage());
//
//            return "redirect:/bpm/workspace-listPersonalTasks.do";
//        }
//
//        return "form/completeTask";
//    }
    
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
