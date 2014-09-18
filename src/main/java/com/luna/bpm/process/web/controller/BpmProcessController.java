package com.luna.bpm.process.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.service.BpmProcessService;
import com.luna.common.entity.enums.BooleanEnum;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;
import com.luna.showcase.sample.entity.Sex;

@Controller
@RequestMapping("/bpm/process/process")
public class BpmProcessController  extends BaseCRUDController<BpmProcess, Long>{
//    private BpmProcessManager bpmProcessManager;
//    private BpmCategoryManager bpmCategoryManager;
//    private BpmConfBaseManager bpmConfBaseManager;
//    private Exportor exportor;
//    private BeanMapper beanMapper = new BeanMapper();
//    private MessageHelper messageHelper;
    private BpmProcessService getBpmProcessService() {
        return (BpmProcessService) baseService;
    }

	public BpmProcessController() {
		setResourceIdentity("bpm:process:process");
    }
	

    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
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
        	BpmProcess bpmProcess = getBpmProcessService().findByName(fieldValue);
            if (bpmProcess == null || (bpmProcess.getId().equals(id) && bpmProcess.getName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该名称已使用");
            }
        }
        return response.result();
    }

	
//    @RequestMapping("bpm-process-export")
//    public void export(@ModelAttribute Page page,
//            @RequestParam Map<String, Object> parameterMap,
//            HttpServletResponse response) throws Exception {
//        List<PropertyFilter> propertyFilters = PropertyFilter
//                .buildFromMap(parameterMap);
//        page = bpmProcessManager.pagedQuery(page, propertyFilters);
//
//        List<BpmProcess> bpmCategories = (List<BpmProcess>) page.getResult();
//        TableModel tableModel = new TableModel();
//        tableModel.setName("bpm-process");
//        tableModel.addHeaders("id", "name");
//        tableModel.setData(bpmCategories);
//        exportor.export(response, tableModel);
//    }

}
