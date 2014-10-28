package com.luna.bpm.process.web.controller;

import javax.validation.Valid;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.bpm.process.service.UserTaskService;
import com.luna.common.Constants;
import com.luna.common.entity.enums.BooleanEnum;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.controller.permission.PermissionList;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/bpm/usertask")
public class UserTaskController {

	private boolean listAlsoSetCommonData = false;

	protected PermissionList permissionList = null;

	@Autowired
	UserTaskService userTaskService;

	public UserTaskController() {
		setResourceIdentity("bpm:process:task");
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
	 * 任务列表
	 * 
	 * @param user
	 *            操作用户
	 * @param taskStatus
	 *            任务状态 prepare preclaim finished claimed
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(@CurrentUser User user,
			@RequestParam("taskstatus") String taskStatus,
			Searchable searchable, Model model) {

//		if (permissionList != null) {
//			this.permissionList.assertHasViewPermission();
//		}

		if (null != taskStatus && "prepare".equals(taskStatus)) {
			model.addAttribute(
					"page",
					userTaskService.findPersonalTasks(
							user.getUsername(), searchable.getPage()));
		}
		else if(null != taskStatus && "preclaim".equals(taskStatus)) {
			model.addAttribute(
					"page",
					userTaskService.findGroupTasks(
							user.getUsername(), searchable.getPage()));
		}
		else if(null != taskStatus && "finished".equals(taskStatus)) {
			model.addAttribute(
					"page",
					userTaskService.findHistoryTasks(
							user.getUsername(), searchable.getPage()));
		}
		else if(null != taskStatus && "claimed".equals(taskStatus)) {
			model.addAttribute(
					"page",
					userTaskService.findDelegatedTasks(
							user.getUsername(), searchable.getPage()));
		}

		if (listAlsoSetCommonData) {
			setCommonData(model);
		}

		// return "bpm/usertask/list?taskstatus="+taskStatus;
		return "bpm/usertask/list";
	}
	
	@RequestMapping(value = "claim")
    public String claim(
    		Model model, 
			@CurrentUser User user,
			@RequestParam("taskstatus") String taskstatus,
			@RequestParam("taskId") String taskId, 
            RedirectAttributes redirectAttributes) {

        try {
        	
        	userTaskService.claim(user.getUsername(), taskId);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "领取成功, 请在代办任务中查看！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
		}
        
        return "redirect:" + "/bpm/usertask?taskstatus="+taskstatus;
    }
	

	@RequestMapping(value = "unclaim")
    public String unclaim(
    		Model model, 
			@CurrentUser User user,
			@RequestParam("taskstatus") String taskstatus,
			@RequestParam("taskId") String taskId, 
            RedirectAttributes redirectAttributes) {

        try {
        	
        	userTaskService.unclaim(taskId);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "释放任务成功！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
		}
        
        return "redirect:/bpm/usertask?taskstatus="+taskstatus;
    }
	

	@RequestMapping(value = "revoke")
    public String revoke(
    		Model model,  
			@CurrentUser User user,
			@RequestParam("taskstatus") String taskstatus,
			@RequestParam("taskId") String taskId, 
            RedirectAttributes redirectAttributes) {

        try {
        	
        	Integer ret = userTaskService.revoke(taskId);
        	if(0 == ret.intValue()){
        		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "撤销任务成功！");
        	}
        	else if(1==ret.intValue()){
        		redirectAttributes.addFlashAttribute(Constants.ERROR, "撤销任务失败，流程已完结！");
        	}
        	else if(2==ret.intValue()){
        		redirectAttributes.addFlashAttribute(Constants.ERROR, "撤销任务失败，流程已执行！");
        	}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
		}
        
        return "redirect:/bpm/usertask?taskstatus="+taskstatus;
    }

}
