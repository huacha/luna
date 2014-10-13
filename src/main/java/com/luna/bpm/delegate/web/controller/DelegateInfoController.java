package com.luna.bpm.delegate.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.bpm.delegate.entity.DelegateInfo;
import com.luna.bpm.delegate.service.DelegateInfoService;
import com.luna.common.Constants;
import com.luna.common.entity.search.Searchable;
import com.luna.common.web.bind.annotation.PageableDefaults;
import com.luna.common.web.controller.BaseController;
import com.luna.common.web.controller.permission.PermissionList;
import com.luna.sys.group.entity.Group;
import com.luna.sys.group.service.GroupService;
import com.luna.sys.user.entity.User;
import com.luna.sys.user.entity.UserStatus;
import com.luna.sys.user.service.UserService;
import com.luna.sys.user.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/bpm/process/delegate")
public class DelegateInfoController extends
		BaseController<DelegateInfo, Long> {
	// private DelegateInfoManager bpmProcessManager;
	// private BpmCategoryManager bpmCategoryManager;
	// private BpmConfBaseManager bpmConfBaseManager;
	// private Exportor exportor;
	// private BeanMapper beanMapper = new BeanMapper();
	// private MessageHelper messageHelper;

    private boolean listAlsoSetCommonData = false;

    protected PermissionList permissionList = null;

	@Autowired
	private DelegateInfoService delegateInfoService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	public DelegateInfoController() {
		String resourceIdentity = "bpm:process:delegate";
		if (!StringUtils.isEmpty(resourceIdentity)) {
            permissionList = PermissionList.newPermissionList(resourceIdentity);
        }
	}

	@Override
	protected void setCommonData(Model model) {
		Map<String, String> attorneyType = new HashMap<String, String>();
		attorneyType.put("1", "单用户");
		attorneyType.put("2", "用户组");
		
		model.addAttribute("attorneyTypeList", attorneyType);
	}

    @RequestMapping(method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String list(Searchable searchable, Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }

        setCommonData(model);

        model.addAttribute("page", delegateInfoService.findAll(searchable));
       
        return viewName("list");
    }
    

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable("id") DelegateInfo m) {

        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }
        
        User assigneeUser=userService.findOne(Long.parseLong(m.getAssignee()));
        if(null != assigneeUser){
        	m.setAssigneeName(assigneeUser.getUsername());
        }
        
        if("1".equals(m.getAttorneyType())){
        	User attorneyUser=userService.findOne(Long.parseLong(m.getAttorney()));
        	if(null != attorneyUser){
        		m.setAttorneyName(attorneyUser.getUsername());
        	}
        }
        else if("2".equals(m.getAttorneyType())){
        	Group attorneyGroup=groupService.findOne(Long.parseLong(m.getAttorney()));
        	if(null != attorneyGroup){
        		m.setAttorneyName(attorneyGroup.getName());
        	}
        }

        setCommonData(model);
        model.addAttribute("m", m);
        model.addAttribute(Constants.OP_NAME, "查看");
        return viewName("editForm");
    }
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String showCreateForm(Model model, @CurrentUser User opUser) {
		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}
		setCommonData(model);

        model.addAttribute(Constants.OP_NAME, "新增");
        if (!model.containsAttribute("m")) {
            model.addAttribute("m", newModel());
        }

		DelegateInfo m = (DelegateInfo) model.asMap().get("m");
		m.setAssignee(String.valueOf(opUser.getId()));
		m.setAssigneeName(opUser.getUsername());
		m.setAttorneyType("1");
		m.setStatus(1);
		
        return viewName("editForm");
	}

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@CurrentUser User opUser, 
            Model model, @Valid @ModelAttribute("m") DelegateInfo m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }

        if (hasError(m, result)) {
            return showCreateForm(model, opUser);
        }
        try {
        	delegateInfoService.save(m);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        
        return redirectToUrl(null);
    }


    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") DelegateInfo m, Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }
        

        User assigneeUser=userService.findOne(Long.parseLong(m.getAssignee()));
        if(null != assigneeUser){
        	m.setAssigneeName(assigneeUser.getUsername());
        }
        
        if("1".equals(m.getAttorneyType())){
        	User attorneyUser=userService.findOne(Long.parseLong(m.getAttorney()));
        	if(null != attorneyUser){
        		m.setAttorneyName(attorneyUser.getUsername());
        	}
        }
        else if("2".equals(m.getAttorneyType())){
        	Group attorneyGroup=groupService.findOne(Long.parseLong(m.getAttorney()));
        	if(null != attorneyGroup){
        		m.setAttorneyName(attorneyGroup.getName());
        	}
        }


        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "修改");
        model.addAttribute("m", m);
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(User user, 
            Model model, @Valid @ModelAttribute("m") DelegateInfo m, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        if (hasError(m, result)) {
            return showUpdateForm(m, model);
        }
        try {
			delegateInfoService.update(m);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        return redirectToUrl(backURL);
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String delete(
            @PathVariable("id") DelegateInfo m,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {


        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        try {
        	delegateInfoService.delete(m);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        return redirectToUrl(backURL);
    }

    @RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteInBatch(
            @RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {


        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        try {
        	delegateInfoService.delete(ids);
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.ERROR, e.getMessage());
			log.error("",e);
		}
        return redirectToUrl(backURL);
    }

	
	//selectType  multiple single
    @RequestMapping(value = {"userselect/{selectType}", "select"}, method = RequestMethod.GET)
    public String userselect(
            Searchable searchable, Model model,
            @PathVariable(value = "selectType") String selectType,
            @MatrixVariable(value = "domId", pathVar = "selectType") String domId,
            @MatrixVariable(value = "domName", pathVar = "selectType", required = false) String domName) {

        this.permissionList.assertHasViewPermission();

        model.addAttribute("selectType", selectType);
        model.addAttribute("domId", domId);
        model.addAttribute("domName", domName);
        
        model.addAttribute("statusList", UserStatus.values());

        model.addAttribute("page", userService.findAll(searchable));
        
        setCommonData(model);

        return "bpm/process/delegate/userselect";
    }
    

	//selectType  multiple single
    @RequestMapping(value = {"groupselect/{selectType}", "select"}, method = RequestMethod.GET)
    public String groupselect(
            Searchable searchable, Model model,
            @PathVariable(value = "selectType") String selectType,
            @MatrixVariable(value = "domId", pathVar = "selectType") String domId,
            @MatrixVariable(value = "domName", pathVar = "selectType", required = false) String domName) {

        this.permissionList.assertHasViewPermission();

        model.addAttribute("selectType", selectType);
        model.addAttribute("domId", domId);
        model.addAttribute("domName", domName);
        
        model.addAttribute("statusList", UserStatus.values());

        model.addAttribute("page", groupService.findAll(searchable));
        
        setCommonData(model);

        return "bpm/process/delegate/groupselect";
    }
}
