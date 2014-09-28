package com.luna.xform.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luna.common.Constants;
import com.luna.common.utils.JsonUtil;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.model.FieldModel;
import com.luna.xform.service.DataService;
import com.luna.xform.service.FormTemplateService;

@RequestMapping("/xform/render")
@Controller
public class FormRenderController {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	FormTemplateService formTemplateService;
	@Autowired
	DataService dataService;
	
	@RequestMapping(value = "{formid}", method = RequestMethod.GET)
	public String list(Model model, @PathVariable("formid") Long formid) throws Exception {
		String sql = formTemplateService.getSelectSql(formid);
		List<FieldModel> fields = formTemplateService.getFields(formid);
		List<Map<String, Object>> list = dataService.findAll(sql);
		model.addAttribute("list", list);
		model.addAttribute("fields", fields);
		model.addAttribute("formid", formid);
		
		return "/xform/render/list";
	}
	
	@RequestMapping(value = "{formid}/create", method = RequestMethod.GET)
	public String showCreateForm(Model model, @PathVariable("formid") Long formid) {
		FormTemplate m = formTemplateService.findOne(formid);
		model.addAttribute("m", m);
		model.addAttribute("formid", formid);
		
		return "/xform/render/editForm";
	}
	
	@RequestMapping(value = "{formid}/create", method = RequestMethod.POST)
	public String create(Model model, @PathVariable("formid") Long formid,HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
		List<FieldModel> fields = formTemplateService.getFields(formid);
		Map<String,String> map = new HashMap<String, String>();
		for (FieldModel fieldModel : fields) {
			String key = fieldModel.getName();
			String val = request.getParameter(key);
			map.put(key, val);
		}
		String sql = formTemplateService.getInsertSql(formid);
		int count = dataService.save(sql, map);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "成功新增: "+count);
		return "redirect:/xform/render/"+formid;
	}
	
	@RequestMapping(value = "{formid}/{dataid}/update", method = RequestMethod.GET)
	public String showUpdateForm(Model model, @PathVariable("formid") Long formid, @PathVariable("dataid") Long dataid) throws IOException {
		FormTemplate m = formTemplateService.findOne(formid);
		model.addAttribute("m", m);
		model.addAttribute("formid", formid);
		model.addAttribute("dataid", dataid);
		String sql = formTemplateService.getSelectSql(formid,dataid);
		Map<String, Object> data = dataService.findOne(sql);
		String json = JsonUtil.toJson(data);
		model.addAttribute("json", json);
		return "/xform/render/editForm";
	}
	
	@RequestMapping(value = "{formid}/{dataid}/update", method = RequestMethod.POST)
	public String update(Model model, @PathVariable("formid") Long formid, @PathVariable("dataid") Long dataid,HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
		List<FieldModel> fields = formTemplateService.getFields(formid);
		Map<String,String> map = new HashMap<String, String>();
		for (FieldModel fieldModel : fields) {
			String key = fieldModel.getName();
			String val = request.getParameter(key);
			map.put(key, val);
		}
		map.put("id", String.valueOf(dataid));
		String sql = formTemplateService.getUpdateSql(formid);
		int count = dataService.update(sql, map);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "成功修改: "+count);
		return "redirect:/xform/render/"+formid;
	}

	@RequestMapping(value = "{formid}/batch/delete", method = { RequestMethod.GET,RequestMethod.POST })
	public String deleteInBatch(@PathVariable("formid") Long formid, @RequestParam(value = "ids", required = false) Long[] ids,RedirectAttributes redirectAttributes) {
		String sql = formTemplateService.getDeleteSql(formid,ids);
		int count = dataService.delete(sql);
		redirectAttributes.addFlashAttribute(Constants.MESSAGE, "成功删除: "+count);
		return "redirect:/xform/render/"+formid;
	}
}
