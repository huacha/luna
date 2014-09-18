package com.luna.bpm.process.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.bpm.process.service.BpmMailTemplateService;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;

@Controller
@RequestMapping(value="/bpm/mailtemplate")
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
	

    /**
     * 验证返回格式
     * 单个：[fieldId, 1|0, msg]
     * 多个：[[fieldId, 1|0, msg],[fieldId, 1|0, msg]]
     *
     * @param fieldId
     * @param fieldValue
     * @return
     */
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id) {
        ValidateResponse response = ValidateResponse.newInstance();

        if ("name".equals(fieldId)) {
        	BpmMailTemplate bpmMailTemplate = getBpmMailTemplateService().findByName(fieldValue);
            if (bpmMailTemplate == null || (bpmMailTemplate.getId().equals(id) && bpmMailTemplate.getName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该名称已使用");
            }
        }
        return response.result();
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
