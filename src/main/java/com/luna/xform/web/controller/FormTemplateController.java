package com.luna.xform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.service.FormTemplateService;

@Controller
@RequestMapping("/xform/template")
public class FormTemplateController extends BaseCRUDController<FormTemplate, Long> {
	
	FormTemplateService getFormTemplateService(){
		return (FormTemplateService) baseService;
	}
	
	public FormTemplateController() {
		setResourceIdentity("xform:template");
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
        return "xform/template/select";
    }

}
