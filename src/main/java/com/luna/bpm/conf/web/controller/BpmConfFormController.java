package com.luna.bpm.conf.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.service.BpmConfFormService;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.common.Constants;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;
import com.luna.maintain.extkeyvalue.service.ExtKeyValueService;

@Controller
@RequestMapping(value="/bpm/conf/form")
public class BpmConfFormController   extends BaseCRUDController<BpmConfForm, Long> {
    private BpmConfFormService getBpmConfFormService() {
        return (BpmConfFormService) baseService;
    }

	public BpmConfFormController() {
		setResourceIdentity("bpm:conf:form");
    }

	@Autowired
	ExtKeyValueService extKeyValueService;
	
	private static final String bpmConfFormType = "BPM_FORM_TYPE";
	private static final String bpmConfDATASOURCE = "BPM_DATA_SOURCE";
	
    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("bpmconfformtype", extKeyValueService.findByExtKeyValueCategoryName(bpmConfFormType));
        model.addAttribute("bpmconfdatasource", extKeyValueService.findByExtKeyValueCategoryName(bpmConfDATASOURCE));
    }

	@RequestMapping(value = "/process-{processId}/node-{nodeId}", method = RequestMethod.GET)
	@PageableDefaults(sort = "id=asc")
	public String list(Searchable searchable,
			@PathVariable("processId") BpmProcess bpmProcess,
			@PathVariable("nodeId") BpmConfNode bpmConfNode, Model model) {
		setCommonData(model);
		if (bpmProcess != null) {
			model.addAttribute("bpmProcess", bpmProcess);
		}
		if (bpmConfNode != null) {
			model.addAttribute("bpmConfNode", bpmConfNode);
			searchable.addSearchFilter("bpmConfNode.id", SearchOperator.eq,
					bpmConfNode.getId());
		}
		return super.list(searchable, model);
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
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "bpmConfNodeId", required = true) BpmConfNode bpmConfNode) {
        ValidateResponse response = ValidateResponse.newInstance();
        BpmConfForm bpmConfForm = getBpmConfFormService().findUnique(fieldValue, bpmConfNode);
        if (bpmConfForm == null || (bpmConfForm.getId().equals(id))) {
            //如果msg 不为空 将弹出提示框
            response.validateSuccess(fieldId, "");
        } else {
            response.validateFail(fieldId, "该表单已配置");
        }
        return response.result();
    }

	@RequestMapping(value = "/node-{bpmConfNodeId}/create", method = RequestMethod.GET)
	public String showCreateForm(Model model,
			@PathVariable("bpmConfNodeId") BpmConfNode bpmConfNode) {
		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}
		setCommonData(model);
		String result = super.showCreateForm(model);
		if (bpmConfNode != null) {
			BpmConfForm m = (BpmConfForm) model.asMap().get("m");
			m.setBpmConfNode(bpmConfNode);
			m.setStatus(1);
		}
		model.addAttribute(Constants.OP_NAME, "新增");
		return result;
	}

	@RequestMapping(value = "/node-{bpmConfNodeId}/create", method = RequestMethod.POST)
	public String create(Model model,
			@Valid @ModelAttribute("m") BpmConfForm bpmConfForm,
			BindingResult result,
			@RequestParam(value = "BackURL", required = false) String backURL,
			RedirectAttributes redirectAttributes) {
		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}
		if (hasError(bpmConfForm, result)) {
			return showCreateForm(model);
		}
		try {
			baseService.save(bpmConfForm);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
		return redirectToUrl(backURL);
	}

	@RequestMapping(value = "/node-{nodeId}/{id}/update", method = RequestMethod.GET)
	public String showBpmConfFormUpdateForm(Model model,
			@PathVariable("nodeId") Long bpmConfNodeId,
			@PathVariable("id") BpmConfForm bpmConfForm,
			@RequestParam(value = "copy", defaultValue = "false") boolean isCopy) {
		this.permissionList.assertHasEditPermission();
		setCommonData(model);
		model.addAttribute(Constants.OP_NAME, isCopy ? "复制" : "修改");
		return super.showUpdateForm(bpmConfForm, model);
	}

	@RequestMapping(value = { "/node-{nodeId}/{id}/update" }, method = RequestMethod.POST)
	public String update(Model model,
			@Valid @ModelAttribute("m") BpmConfForm bpmConfForm,
			BindingResult result,
			@RequestParam(value = "BackURL", required = false) String backURL,
			RedirectAttributes redirectAttributes) {
		return super.update(model, bpmConfForm, result, backURL,
				redirectAttributes);
	}

	
    @RequestMapping(value = "/node-{nodeId}/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public BpmConfForm deleteBpmConfForm(@PathVariable("id") BpmConfForm bpmConfForm) {
        this.permissionList.assertHasEditPermission();
        try {
			getBpmConfFormService().delete(bpmConfForm);
		} catch (Exception e) {
			log.error("",e);
		}
        return bpmConfForm;
    }


    @RequestMapping(value = "/node-{nodeId}/batch/delete")
    @ResponseBody
    public Object deleteBpmConfFormInBatch(@RequestParam(value = "ids", required = false) Long[] ids) {
        this.permissionList.assertHasEditPermission();
        try {
			getBpmConfFormService().delete(ids);
		} catch (Exception e) {
			log.error("",e);
		}
        return redirectToUrl(null);
    }

}
