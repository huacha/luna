package com.luna.bpm.process.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.bpm.delegate.entity.DelegateInfo;
import com.luna.bpm.process.service.UserProcessService;
import com.luna.common.Constants;
import com.luna.common.entity.enums.BooleanEnum;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.controller.permission.PermissionList;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/bpm/userprocess")
public class UserProcessController {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private boolean listAlsoSetCommonData = false;

	protected PermissionList permissionList = null;

	@Autowired
	UserProcessService userProcessService;

	public UserProcessController() {
		setResourceIdentity("bpm:usertask:userprocess");
	}

	/**
	 * 权限前缀：如sys:user 则生成的新增权限为 sys:user:create
	 */
	public void setResourceIdentity(String resourceIdentity) {
		if (!StringUtils.isEmpty(resourceIdentity)) {
			permissionList = PermissionList.newPermissionList(resourceIdentity);
		}
	}

	protected void setCommonData(Model model) {
		model.addAttribute("booleanList", BooleanEnum.values());
	}

	/**
	 * 流程列表
	 * 
	 * @param user
	 *            操作用户
	 * @param processStatus
	 *            流程状态 unfinished finished involve
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(@CurrentUser User user,
			@RequestParam("processstatus") String processStatus,
			Searchable searchable, Model model) {

//		if (permissionList != null) {
//			this.permissionList.assertHasViewPermission();
//		}

		if (null != processStatus && "involve".equals(processStatus)) {
			model.addAttribute(
					"page",
					userProcessService.findInvolvedProcessInstances(
							String.valueOf(user.getId()), searchable.getPage()));
		}
		else if(null != processStatus && "unfinished".equals(processStatus)) {
			model.addAttribute(
					"page",
					userProcessService.findRunningProcessInstances(
							String.valueOf(user.getId()), searchable.getPage()));
		}
		else if(null != processStatus && "finished".equals(processStatus)) {
			model.addAttribute(
					"page",
					userProcessService.findCompletedProcessInstances(
							String.valueOf(user.getId()), searchable.getPage()));
		}
		if (listAlsoSetCommonData) {
			setCommonData(model);
		}

		return "bpm/userprocess/list";
	}
	
	@RequestMapping(value = "{id}/terminate")
    public String delete(
            @PathVariable("id") String processInstanceId,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
//        if (permissionList != null) {
//            this.permissionList.assertHasDeletePermission();
//        }

        try {
        	userProcessService.terminateProcessInstanceById(processInstanceId);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "终止成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        return "redirect:/bpm/userprocess?processstatus=unfinished";
    }
}
