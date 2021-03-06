package com.luna.bpm.modeler.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.EditorJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luna.bpm.conf.service.BpmConfBaseService;
import com.luna.bpm.process.cmd.SyncProcessCmd;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.service.BpmProcessService;
import com.luna.common.Constants;
import com.luna.common.utils.JsonUtil;

@Controller
@RequestMapping("modeler")
public class ModelerController {
	private static Logger logger = LoggerFactory.getLogger(ModelerController.class);
	@Autowired
    private ProcessEngine processEngine;
	@Autowired
	private SyncProcessCmd syncProcessCmd;
	

	@Autowired
	private BpmProcessService bpmProcessService;
	@Autowired
	private BpmConfBaseService bpmConfBaseService;
	
	private final static String redirectUrl = "redirect:/modeler";

	/**
	 * 查看已有流程模型列表
	 * @param model
	 * @return
	 */
    @RequestMapping()
    public String list(org.springframework.ui.Model model) {
        List<Model> models = processEngine.getRepositoryService().createModelQuery().list();
        model.addAttribute("models", models);
        return "bpm/modeler/list";
    }
    

	/**
	 * 发布流程请求
	 * @param model
	 * @return
	 */
    @RequestMapping(value="deployModel", method = RequestMethod.GET)
    public String deployView(org.springframework.ui.Model model,
    		@RequestParam(value = "modelId", required = false) String modelId) {
    	BpmProcess bpmProcess = new BpmProcess();
    	bpmProcess.setPriority(50);
        model.addAttribute("modelId", modelId);
        model.addAttribute("m", bpmProcess);
        
        return "bpm/modeler/editForm";
    }
    
    /**
     * 将模型发布为流程
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="deployModel", method = RequestMethod.POST)
    public String deploy(@RequestParam("modelId") String modelId, 
    		@ModelAttribute("m") BpmProcess bpmProcess,
    		RedirectAttributes redirectAttributes) throws Exception {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Model modelData = repositoryService.getModel(modelId);
        byte[] content = repositoryService.getModelEditorSource(modelData.getId());
        if(content == null){
        	redirectAttributes.addFlashAttribute(Constants.ERROR, "未找到模型，流程不能发布！");
        	return redirectUrl;
        }
        JsonNode modelNode = (JsonNode) new ObjectMapper().readTree(content);
        if (modelNode.get(EditorJsonConstants.EDITOR_CHILD_SHAPES) == null) {
        	redirectAttributes.addFlashAttribute(Constants.ERROR, "模型中没有流程图，流程不能发布！");
        	return redirectUrl;
		}

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
        
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        syncProcessCmd.setProcessDefinitionId(processDefinition.getId());
    	syncProcessCmd.setProcessDefinitionName(modelData.getName());
        processEngine.getManagementService().executeCommand(syncProcessCmd);
        
        bpmProcess.setCode(processDefinition.getId());
        bpmProcess.setBpmConfBase(bpmConfBaseService.findByProcessDefinitionId(processDefinition.getId()));
        
        bpmProcessService.save(bpmProcess);
        
        
        /*
        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).list();

        for (ProcessDefinition processDefinition : processDefinitions) {
        	syncProcessCmd.setProcessDefinitionId(processDefinition.getId());
        	syncProcessCmd.setProcessDefinitionName(modelData.getName());
            processEngine.getManagementService().executeCommand(syncProcessCmd);
        }
        */
    	
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "流程模型["+ modelData.getName() + "]已发布成["
        				+bpmProcess.getName()+"],请在流程配置中配置相关参数！");
        return redirectUrl;
    }



    /**
     * 在modeler中打开或新建一个模型
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("create")
    public String open(@RequestParam(value = "id", required = false) String id) throws Exception {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Model model = repositoryService.getModel(id);
        if (model == null) {
            model = repositoryService.newModel();
            repositoryService.saveModel(model);
            id = model.getId();
        }
        return "redirect:/widgets/modeler/editor.html?id=" + id;
    }
    
    /**
     * 删除一个模型
     */
    @RequestMapping("remove")
    public String remove(@RequestParam("id") String id,
    		RedirectAttributes redirectAttributes) {
    	
    	RepositoryService repositoryService = processEngine.getRepositoryService();
        Model model = repositoryService.createModelQuery().modelId(id).singleResult();
        
        
        if(null == model.getDeploymentId() || 0 == model.getDeploymentId().length()){
        	processEngine.getRepositoryService().deleteModel(id);
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "模型删除成功！");
        }
        else{
        	redirectAttributes.addFlashAttribute(Constants.ERROR, "模型已发布，不能删除！");
        }
        
        
        return redirectUrl;
    }

    /**
     * 将模型发布为流程
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("deploy")
    public String deploy(@RequestParam("id") String id, 
    		RedirectAttributes redirectAttributes) throws Exception {
    	
    	
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Model modelData = repositoryService.getModel(id);
        byte[] content = repositoryService.getModelEditorSource(modelData.getId());
        if(content == null){
        	redirectAttributes.addFlashAttribute(Constants.ERROR, "未找到模型，流程不能发布！");
        	return redirectUrl;
        }
        JsonNode modelNode = (JsonNode) new ObjectMapper().readTree(content);
        if (modelNode.get(EditorJsonConstants.EDITOR_CHILD_SHAPES) == null) {
        	redirectAttributes.addFlashAttribute(Constants.ERROR, "模型中没有流程图，流程不能发布！");
        	return redirectUrl;
		}

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);

        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).list();

        for (ProcessDefinition processDefinition : processDefinitions) {
        	syncProcessCmd.setProcessDefinitionId(processDefinition.getId());
        	syncProcessCmd.setProcessDefinitionName(modelData.getName());
            processEngine.getManagementService().executeCommand(syncProcessCmd);
        }
    	
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, processName+" 已发布！");
        return redirectUrl;
    }

    /**
     * oryx.debug.js 打开modeler时会请求此方法
     * 请求的url必须为 /modeler/model
     * 
     * @param modelId
     * @return json
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("model")
    @ResponseBody
    public String getModel(@RequestParam("id") String modelId) throws Exception {
    	RepositoryService repositoryService = processEngine.getRepositoryService();
        Model model = repositoryService.getModel(modelId);

        if (model == null) {
            model = repositoryService.newModel();
            model.setName("");
            model.setMetaInfo("");
            model.setVersion(1);
            model.setMetaInfo("");
            repositoryService.saveModel(model);
        }

        Map root = new HashMap();
        root.put("modelId", model.getId());
        root.put("name", model.getName());
        root.put("revision", model.getVersion());
        root.put("description", model.getMetaInfo());

        byte[] bytes = repositoryService.getModelEditorSource(model.getId());

        if (bytes != null) {
            String modelEditorSource = new String(bytes, "utf-8");
            Map modelNode = JsonUtil.toObj(modelEditorSource, Map.class);
            root.put("model", modelNode);
        } else {
            Map modelNode = new HashMap();
            modelNode.put("id", "canvas");
            modelNode.put("resourceId", "canvas");

            Map stencilSetNode = new HashMap();
            stencilSetNode.put("namespace",
                    "http://b3mn.org/stencilset/bpmn2.0#");
            modelNode.put("stencilset", stencilSetNode);

            model.setMetaInfo(JsonUtil.toJson(root));
            model.setName("name");
            model.setKey("key");

            root.put("model", modelNode);
        }

        logger.info("model : {}", root);
    	return JsonUtil.toJson(root);
	}
    
    /**
     * oryx.debug.js 保存model时会请求此方法
     * 请求的url必须为 /modeler/save
     * 
     * @param glossayXml
     * @param id
     * @param description
     * @param jsonXml
     * @param name
     * @param namespace
     * @param parent
     * @param svgXml
     * @param type
     * @param views
     * @return
     * @throws Exception
     */
	@RequestMapping("save")
	@ResponseBody
	public String save(@RequestParam("glossary_xml") String glossayXml,
			@RequestParam("id") String id,
			@RequestParam("description") String description,
			@RequestParam("json_xml") String jsonXml,
			@RequestParam("name") String name,
			@RequestParam("namespace") String namespace,
			@RequestParam("parent") String parent,
			@RequestParam("svg_xml") String svgXml,
			@RequestParam("type") String type,
			@RequestParam("views") String views) throws Exception {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Model model = repositoryService.getModel(id);
		model.setName(name);
		model.setMetaInfo(description);
		
		logger.info("jsonXml : {}",jsonXml);
		repositoryService.saveModel(model);
		repositoryService.addModelEditorSource(model.getId(),jsonXml.getBytes("utf-8"));
		return "";
	}

}
