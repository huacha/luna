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

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfRule;
import com.luna.bpm.conf.service.BpmConfRuleService;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.common.Constants;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;
import com.luna.maintain.extkeyvalue.service.ExtKeyValueService;

@Controller
@RequestMapping(value="/bpm/conf/rule")
public class BpmConfRuleController   extends BaseCRUDController<BpmConfRule, Long> {
    private BpmConfRuleService getBpmConfRuleService() {
        return (BpmConfRuleService) baseService;
    }

	
	public BpmConfRuleController() {
		setResourceIdentity("bpm:conf:rule");
    }

	@Autowired
	ExtKeyValueService extKeyValueService;
	
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
   
        BpmConfRule bpmConfRule = getBpmConfRuleService().findUnique(fieldValue, bpmConfNode);
        if (bpmConfRule == null || (bpmConfRule.getId().equals(id))) {
            //如果msg 不为空 将弹出提示框
            response.validateSuccess(fieldId, "");
        } else {
            response.validateFail(fieldId, "该规则已使用");
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
			BpmConfRule m = (BpmConfRule) model.asMap().get("m");
			m.setBpmConfNode(bpmConfNode);
		}
		model.addAttribute(Constants.OP_NAME, "新增");

		return result;
	}

	@RequestMapping(value = "/node-{bpmConfNodeId}/create", method = RequestMethod.POST)
	public String create(Model model,
			@Valid @ModelAttribute("m") BpmConfRule bpmConfRule,
			BindingResult result,
			@RequestParam(value = "BackURL", required = false) String backURL,
			RedirectAttributes redirectAttributes) {

		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}

		if (hasError(bpmConfRule, result)) {
			return showCreateForm(model);
		}
		baseService.save(bpmConfRule);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
		return redirectToUrl(backURL);
	}

	@RequestMapping(value = "/node-{nodeId}/{id}/update", method = RequestMethod.GET)
	public String showBpmConfRuleUpdateForm(Model model,
			@PathVariable("nodeId") Long bpmConfNodeId,
			@PathVariable("id") BpmConfRule bpmConfRule,
			@RequestParam(value = "copy", defaultValue = "false") boolean isCopy) {

		this.permissionList.assertHasEditPermission();

		setCommonData(model);
		model.addAttribute(Constants.OP_NAME, isCopy ? "复制" : "修改");

		return super.showUpdateForm(bpmConfRule, model);
	}

	@RequestMapping(value = { "/node-{nodeId}/{id}/update" }, method = RequestMethod.POST)
	public String update(Model model,
			@Valid @ModelAttribute("m") BpmConfRule bpmConfRule,
			BindingResult result,
			@RequestParam(value = "BackURL", required = false) String backURL,
			RedirectAttributes redirectAttributes) {

		return super.update(model, bpmConfRule, result, backURL,
				redirectAttributes);
	}

	
    @RequestMapping(value = "/node-{nodeId}/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public BpmConfRule deleteBpmConfRule(@PathVariable("id") BpmConfRule bpmConfRule) {

        this.permissionList.assertHasEditPermission();

        getBpmConfRuleService().delete(bpmConfRule);
        return bpmConfRule;
    }


    @RequestMapping(value = "/node-{nodeId}/batch/delete")
    @ResponseBody
    public Object deleteBpmConfRuleInBatch(@RequestParam(value = "ids", required = false) Long[] ids) {

        this.permissionList.assertHasEditPermission();

        getBpmConfRuleService().delete(ids);
        //return ids;

        return redirectToUrl(null);
    }

}
