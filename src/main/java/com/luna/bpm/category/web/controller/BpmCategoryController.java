package com.luna.bpm.category.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.category.service.BpmCategoryService;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;
import com.luna.showcase.sample.entity.Sample;

@Controller
@RequestMapping("/bpm/category")
public class BpmCategoryController extends BaseCRUDController<BpmCategory, Long>{
	
    private BpmCategoryService getBpmCategoryService() {
        return (BpmCategoryService) baseService;
    }

	public BpmCategoryController() {
		setResourceIdentity("bpm:category");
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
            BpmCategory bpmCategory = getBpmCategoryService().findByName(fieldValue);
            if (bpmCategory == null || (bpmCategory.getId().equals(id) && bpmCategory.getName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "该名称已使用");
            }
        }
        return response.result();
    }

//    @RequestMapping("bpm-category-export")
//    public void export(@ModelAttribute Page page,
//            @RequestParam Map<String, Object> parameterMap,
//            HttpServletResponse response) throws Exception {
//        List<PropertyFilter> propertyFilters = PropertyFilter.buildFromMap(parameterMap);
//        page = bpmCategoryManager.pagedQuery(page, propertyFilters);
//
//        List<BpmCategory> bpmCategories = (List<BpmCategory>) page.getResult();
//        TableModel tableModel = new TableModel();
//        tableModel.setName("bpm-category");
//        tableModel.addHeaders("id", "name");
//        tableModel.setData(bpmCategories);
//        exportor.export(response, tableModel);
//    }

}
