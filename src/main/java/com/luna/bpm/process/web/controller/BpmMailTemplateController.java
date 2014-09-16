package com.luna.bpm.process.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.bpm.process.service.BpmMailTemplateService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/process/mailtemplate")
public class BpmMailTemplateController  extends BaseCRUDController<BpmMailTemplate, Long> {
	
//    private BpmMailTemplateManager bpmMailTemplateManager;
//    private Exportor exportor;
//    private BeanMapper beanMapper = new BeanMapper();
//    private MessageHelper messageHelper;
    private BpmMailTemplateService getBpmMailTemplateService() {
        return (BpmMailTemplateService) baseService;
    }

	
	public BpmMailTemplateController() {
		setResourceIdentity("bpm:process:mailtemplate");
    }

   
//    @RequestMapping("bpm-mail-template-export")
//    public void export(@ModelAttribute Page page,
//            @RequestParam Map<String, Object> parameterMap,
//            HttpServletResponse response) throws Exception {
//        List<PropertyFilter> propertyFilters = PropertyFilter
//                .buildFromMap(parameterMap);
//        page = bpmMailTemplateManager.pagedQuery(page, propertyFilters);
//
//        List<BpmMailTemplate> bpmCategories = (List<BpmMailTemplate>) page.getResult();
//        TableModel tableModel = new TableModel();
//        tableModel.setName("bpm-process");
//        tableModel.addHeaders("id", "name");
//        tableModel.setData(bpmCategories);
//        exportor.export(response, tableModel);
//    }
}
