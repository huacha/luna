package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.conf.service.BpmConfBaseService;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;

@Controller
@RequestMapping("/bpm/conf/base")
public class BpmConfBaseController extends BaseCRUDController<BpmConfBase, Long>{
	
    private BpmConfBaseService getBpmConfBaseService() {
        return (BpmConfBaseService) baseService;
    }

	public BpmConfBaseController() {
		setResourceIdentity("bpm:conf:base");
    }
    
    //selectType  multiple single
    @RequestMapping(value = {"select/{selectType}", "select"}, method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String select(
            Searchable searchable, Model model,
            @PathVariable(value = "selectType") String selectType,
            @MatrixVariable(value = "domId", pathVar = "selectType") String domId,
            @MatrixVariable(value = "domName", pathVar = "selectType", required = false) String domName) {

        this.permissionList.assertHasViewPermission();

        model.addAttribute("selectType", selectType);
        model.addAttribute("domId", domId);
        model.addAttribute("domName", domName);

        super.list(searchable, model);
        return "bpm/conf/base/select";
    }

//    @RequestMapping("bpm-category-export")
//    public void export(@ModelAttribute Page page,
//            @RequestParam Map<String, Object> parameterMap,
//            HttpServletResponse response) throws Exception {
//        List<PropertyFilter> propertyFilters = PropertyFilter.buildFromMap(parameterMap);
//        page = bpmCategoryManager.pagedQuery(page, propertyFilters);
//
//        List<BpmConfBase> bpmCategories = (List<BpmConfBase>) page.getResult();
//        TableModel tableModel = new TableModel();
//        tableModel.setName("bpm-category");
//        tableModel.addHeaders("id", "name");
//        tableModel.setData(bpmCategories);
//        exportor.export(response, tableModel);
//    }

}
