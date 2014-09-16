package com.luna.bpm.category.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.category.service.BpmCategoryService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping("/bpm/category")
public class BpmCategoryController extends BaseCRUDController<BpmCategory, Long>{
	
    private BpmCategoryService getBpmCategoryService() {
        return (BpmCategoryService) baseService;
    }

	public BpmCategoryController() {
		setResourceIdentity("bpm:category");
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
