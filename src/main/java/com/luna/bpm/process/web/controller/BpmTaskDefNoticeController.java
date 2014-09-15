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
import com.luna.bpm.process.entity.BpmTaskDefNotice;
import com.luna.bpm.process.repository.BpmMailTemplateManager;
import com.luna.bpm.process.repository.BpmProcessManager;
import com.luna.bpm.process.repository.BpmTaskDefNoticeManager;
import com.luna.common.Constants;
import com.luna.common.utils.BeanMapper;
import com.luna.common.utils.export.Exportor;
import com.luna.common.utils.export.TableModel;

@Controller
@RequestMapping("bpm")
public class BpmTaskDefNoticeController {
    private BpmTaskDefNoticeManager bpmTaskDefNoticeManager;
    private BpmProcessManager bpmProcessManager;
    private BpmMailTemplateManager bpmMailTemplateManager;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private MessageHelper messageHelper;

    @RequestMapping("bpm-task-def-notice-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        page = bpmTaskDefNoticeManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "bpm/bpm-task-def-notice-list";
    }

    @RequestMapping("bpm-task-def-notice-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        if (id != null) {
            BpmTaskDefNotice bpmTaskDefNotice = bpmTaskDefNoticeManager.getOne(id);
            model.addAttribute("model", bpmTaskDefNotice);
        }

        List<BpmMailTemplate> bpmMailTemplates = bpmMailTemplateManager
                .findAll();
        model.addAttribute("bpmMailTemplates", bpmMailTemplates);

        return "bpm/bpm-task-def-notice-input";
    }

    @RequestMapping("bpm-task-def-notice-save")
    public String save(@ModelAttribute BpmTaskDefNotice bpmTaskDefNotice,
            @RequestParam("bpmProcessId") Long bpmProcessId,
            @RequestParam("bpmMailTemplateId") Long bpmMailTemplateId,
            RedirectAttributes redirectAttributes) {
        BpmTaskDefNotice dest = null;
        Long id = bpmTaskDefNotice.getId();

        if (id != null) {
            dest = bpmTaskDefNoticeManager.findOne(id);
            beanMapper.copy(bpmTaskDefNotice, dest);
        } else {
            dest = bpmTaskDefNotice;
        }

        dest.setBpmProcess(bpmProcessManager.findOne(bpmProcessId));
        dest.setBpmMailTemplate(bpmMailTemplateManager.findOne(bpmMailTemplateId));
        bpmTaskDefNoticeManager.save(dest);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE,"保存成功");

        return "redirect:/bpm/bpm-task-def-notice-list.do?bpmProcessId="
                + bpmProcessId;
    }

    @RequestMapping("bpm-task-def-notice-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            @RequestParam("bpmProcessId") Long bpmProcessId,
            RedirectAttributes redirectAttributes) {
        List<BpmTaskDefNotice> bpmCategories = bpmTaskDefNoticeManager
                .findByIds(selectedItem);
        bpmTaskDefNoticeManager.delete(bpmCategories);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE,"删除成功");

        return "redirect:/bpm/bpm-task-def-notice-list.do?bpmProcessId="
                + bpmProcessId;
    }

    @RequestMapping("bpm-task-def-notice-export")
    public void export(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap,
            HttpServletResponse response) throws Exception {
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        page = bpmTaskDefNoticeManager.pagedQuery(page, propertyFilters);

        List<BpmTaskDefNotice> bpmCategories = (List<BpmTaskDefNotice>) page
                .getResult();
        TableModel tableModel = new TableModel();
        tableModel.setName("bpm-process");
        tableModel.addHeaders("id", "name");
        tableModel.setData(bpmCategories);
        exportor.export(response, tableModel);
    }

    @RequestMapping("bpm-task-def-notice-removeNotice")
    public String removeNotice(@RequestParam("id") Long id) {
        BpmTaskDefNotice bpmTaskDefNotice = bpmTaskDefNoticeManager.findOne(id);
        Long bpmProcessId = bpmTaskDefNotice.getBpmProcess().getId();
        bpmTaskDefNoticeManager.delete(bpmTaskDefNotice);

        return "redirect:/bpm/bpm-task-def-notice-list.do?bpmProcessId="
                + bpmProcessId;
    }

    // ~ ======================================================================
    @Resource
    public void setBpmTaskDefNoticeManager(
            BpmTaskDefNoticeManager bpmTaskDefNoticeManager) {
        this.bpmTaskDefNoticeManager = bpmTaskDefNoticeManager;
    }

    @Resource
    public void setBpmProcessManager(BpmProcessManager bpmProcessManager) {
        this.bpmProcessManager = bpmProcessManager;
    }

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
