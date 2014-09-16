package com.luna.bpm.process.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.hibernate.pretty.MessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.serializer.PropertyFilter;
import com.luna.bpm.Page;
import com.luna.bpm.process.entity.BpmMailTemplate;
import com.luna.bpm.process.repository.BpmMailTemplateManager;
import com.luna.bpm.process.repository.BpmProcessManager;
import com.luna.common.utils.BeanMapper;
import com.luna.common.utils.export.Exportor;
import com.luna.common.utils.export.TableModel;

@Controller
@RequestMapping("bpm")
public class BpmMailTemplateController {
    private BpmMailTemplateManager bpmMailTemplateManager;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private MessageHelper messageHelper;

    @RequestMapping("bpm-mail-template-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        page = bpmMailTemplateManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "bpm/bpm-mail-template-list";
    }

    @RequestMapping("bpm-mail-template-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        if (id != null) {
            BpmMailTemplate bpmMailTemplate = bpmMailTemplateManager.findOne(id);
            model.addAttribute("model", bpmMailTemplate);
        }

        return "bpm/bpm-mail-template-input";
    }

    @RequestMapping("bpm-mail-template-save")
    public String save(@ModelAttribute BpmMailTemplate bpmMailTemplate,
            RedirectAttributes redirectAttributes) {
        BpmMailTemplate dest = null;
        Long id = bpmMailTemplate.getId();

        if (id != null) {
            dest = bpmMailTemplateManager.findOne(id);
            beanMapper.copy(bpmMailTemplate, dest);
        } else {
            dest = bpmMailTemplate;
        }

        bpmMailTemplateManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/bpm/bpm-mail-template-list.do";
    }

    @RequestMapping("bpm-mail-template-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        List<BpmMailTemplate> bpmCategories = bpmMailTemplateManager
                .findByIds(selectedItem);
        bpmMailTemplateManager.delete(bpmCategories);
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/bpm/bpm-mail-template-list.do";
    }

    @RequestMapping("bpm-mail-template-export")
    public void export(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap,
            HttpServletResponse response) throws Exception {
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        page = bpmMailTemplateManager.pagedQuery(page, propertyFilters);

        List<BpmMailTemplate> bpmCategories = (List<BpmMailTemplate>) page.getResult();
        TableModel tableModel = new TableModel();
        tableModel.setName("bpm-process");
        tableModel.addHeaders("id", "name");
        tableModel.setData(bpmCategories);
        exportor.export(response, tableModel);
    }

    // ~ ======================================================================
    @Resource
    public void setBpmMailTemplateManager(
            BpmMailTemplateManager bpmMailTemplateManager) {
        this.bpmMailTemplateManager = bpmMailTemplateManager;
    }

    @Resource
    public void setExportor(Exportor exportor) {
        this.exportor = exportor;
    }

    @Resource
    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }
}