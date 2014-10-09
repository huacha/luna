package com.luna.bpm.process.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.process.entity.DelegateInfo;
import com.luna.bpm.process.service.DelegateInfoService;
import com.luna.common.Constants;
import com.luna.common.entity.enums.BooleanEnum;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping("/bpm/process/delegate")
public class DelegateInfoController extends
		BaseCRUDController<DelegateInfo, Long> {
	// private DelegateInfoManager bpmProcessManager;
	// private BpmCategoryManager bpmCategoryManager;
	// private BpmConfBaseManager bpmConfBaseManager;
	// private Exportor exportor;
	// private BeanMapper beanMapper = new BeanMapper();
	// private MessageHelper messageHelper;
	private DelegateInfoService getDelegateInfoService() {
		return (DelegateInfoService) baseService;
	}

	public DelegateInfoController() {
		setResourceIdentity("bpm:process:delegate");
	}

	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("booleanList", BooleanEnum.values());
	}

	/**
	 * 验证返回格式 单个：[fieldId, 1|0, msg] 多个：[[fieldId, 1|0, msg],[fieldId, 1|0,
	 * msg]]
	 *
	 * @param fieldId
	 * @param fieldValue
	 * @return
	 */
	// @RequestMapping(value = "validate", method = RequestMethod.GET)
	// @ResponseBody
	// public Object validate(
	// @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue")
	// String fieldValue,
	// @RequestParam(value = "id", required = false) Long id) {
	// ValidateResponse response = ValidateResponse.newInstance();
	//
	// if ("name".equals(fieldId)) {
	// DelegateInfo bpmProcess =
	// getDelegateInfoService().findByName(fieldValue);
	// if (bpmProcess == null || (bpmProcess.getId().equals(id) &&
	// bpmProcess.getName().equals(fieldValue))) {
	// //如果msg 不为空 将弹出提示框
	// response.validateSuccess(fieldId, "");
	// } else {
	// response.validateFail(fieldId, "该名称已使用");
	// }
	// }
	// return response.result();
	// }

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String showCreateForm(Model model) {
		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}
		setCommonData(model);
		String result = super.showCreateForm(model);

		DelegateInfo m = (DelegateInfo) model.asMap().get("m");
		m.setStatus(1);
		model.addAttribute(Constants.OP_NAME, "新增");
		return result;
	}
}
