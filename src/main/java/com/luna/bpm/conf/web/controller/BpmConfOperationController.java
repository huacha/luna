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
import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.bpm.conf.service.BpmConfOperationService;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.common.Constants;
import com.luna.common.entity.search.SearchOperator;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseCRUDController;
import com.luna.common.web.validate.ValidateResponse;
import com.luna.maintain.extkeyvalue.service.ExtKeyValueService;

@Controller
@RequestMapping(value="/bpm/conf/operation")
public class BpmConfOperationController   extends BaseCRUDController<BpmConfOperation, Long> {
    private BpmConfOperationService getBpmConfOperationService() {
        return (BpmConfOperationService) baseService;
    }

	
	public BpmConfOperationController() {
		setResourceIdentity("bpm:conf:operation");
    }

	@Autowired
	ExtKeyValueService extKeyValueService;
	
	private static final String bpmConfOperationType = "BPM_OPERATE_TYPE";
	
    @Override
    protected void setCommonData(Model model) {
        model.addAttribute("bpmconfoperatetype", extKeyValueService.findByExtKeyValueCategoryName(bpmConfOperationType));
    }

	@RequestMapping(value = "/process-{processId}/node-{nodeId}", method = RequestMethod.GET)
	@PageableDefaults(sort = "priority=asc")
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
   
        BpmConfOperation bpmConfOperation = getBpmConfOperationService().findUnique(fieldValue, bpmConfNode);
        if (bpmConfOperation == null || (bpmConfOperation.getId().equals(id))) {
            //如果msg 不为空 将弹出提示框
            response.validateSuccess(fieldId, "");
        } else {
            response.validateFail(fieldId, "该名称已使用");
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
			BpmConfOperation m = (BpmConfOperation) model.asMap().get("m");
			m.setBpmConfNode(bpmConfNode);
			m.setPriority(1);
		}
		model.addAttribute(Constants.OP_NAME, "新增");

		return result;
	}

	@RequestMapping(value = "/node-{bpmConfNodeId}/create", method = RequestMethod.POST)
	public String create(Model model,
			@Valid @ModelAttribute("m") BpmConfOperation bpmConfOperation,
			BindingResult result,
			@RequestParam(value = "BackURL", required = false) String backURL,
			RedirectAttributes redirectAttributes) {

		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}

		if (hasError(bpmConfOperation, result)) {
			return showCreateForm(model);
		}
		baseService.save(bpmConfOperation);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
		return redirectToUrl(backURL);
	}

	@RequestMapping(value = "/node-{nodeId}/{id}/update", method = RequestMethod.GET)
	public String showBpmConfOperationUpdateForm(Model model,
			@PathVariable("nodeId") Long bpmConfNodeId,
			@PathVariable("id") BpmConfOperation bpmConfOperation,
			@RequestParam(value = "copy", defaultValue = "false") boolean isCopy) {

		this.permissionList.assertHasEditPermission();

		setCommonData(model);
		model.addAttribute(Constants.OP_NAME, isCopy ? "复制" : "修改");

		return super.showUpdateForm(bpmConfOperation, model);
	}

	@RequestMapping(value = { "/node-{nodeId}/{id}/update" }, method = RequestMethod.POST)
	public String update(Model model,
			@Valid @ModelAttribute("m") BpmConfOperation bpmConfOperation,
			BindingResult result,
			@RequestParam(value = "BackURL", required = false) String backURL,
			RedirectAttributes redirectAttributes) {

		return super.update(model, bpmConfOperation, result, backURL,
				redirectAttributes);
	}

	
    @RequestMapping(value = "/node-{nodeId}/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public BpmConfOperation deleteBpmConfOperation(@PathVariable("id") BpmConfOperation bpmConfOperation) {

        this.permissionList.assertHasEditPermission();

        getBpmConfOperationService().delete(bpmConfOperation);
        return bpmConfOperation;
    }


    @RequestMapping(value = "/node-{nodeId}/batch/delete")
    @ResponseBody
    public Object deleteBpmConfOperationInBatch(@RequestParam(value = "ids", required = false) Long[] ids) {

        this.permissionList.assertHasEditPermission();

        getBpmConfOperationService().delete(ids);
        //return ids;

        return redirectToUrl(null);
    }

}
