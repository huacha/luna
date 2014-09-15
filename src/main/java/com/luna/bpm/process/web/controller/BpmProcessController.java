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
import com.luna.bpm.category.entity.BpmCategory;
import com.luna.bpm.category.repository.BpmCategoryManager;
import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.conf.repository.BpmConfBaseManager;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.repository.BpmMailTemplateManager;
import com.luna.bpm.process.repository.BpmProcessManager;
import com.luna.bpm.process.repository.BpmTaskDefManager;
import com.luna.bpm.process.repository.BpmTaskDefNoticeManager;
import com.luna.common.utils.BeanMapper;
import com.luna.common.utils.export.Exportor;
import com.luna.common.utils.export.TableModel;

@Controller
@RequestMapping("bpm")
public class BpmProcessController {
    private BpmProcessManager bpmProcessManager;
    private BpmCategoryManager bpmCategoryManager;
    private BpmConfBaseManager bpmConfBaseManager;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private MessageHelper messageHelper;

    @RequestMapping("bpm-process-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        List<PropertyFilter> propertyFilters = PropertyFilter.buildFromMap(parameterMap);
        page = bpmProcessManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "bpm/bpm-process-list";
    }

    @RequestMapping("bpm-process-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        if (id != null) {
            BpmProcess bpmProcess = bpmProcessManager.findOne(id);
            model.addAttribute("model", bpmProcess);
        }

        List<BpmCategory> bpmCategories = bpmCategoryManager.findAll();
        List<BpmConfBase> bpmConfBases = bpmConfBaseManager.findAll();
        model.addAttribute("bpmCategories", bpmCategories);
        model.addAttribute("bpmConfBases", bpmConfBases);

        return "bpm/bpm-process-input";
    }

    @RequestMapping("bpm-process-save")
    public String save(@ModelAttribute BpmProcess bpmProcess,
            @RequestParam("bpmCategoryId") Long bpmCategoryId,
            @RequestParam("bpmConfBaseId") Long bpmConfBaseId,
            RedirectAttributes redirectAttributes) {
        BpmProcess dest = null;
        Long id = bpmProcess.getId();

        if (id != null) {
            dest = bpmProcessManager.findOne(id);
            beanMapper.copy(bpmProcess, dest);
        } else {
            dest = bpmProcess;
        }

        dest.setBpmCategory(bpmCategoryManager.findOne(bpmCategoryId));
        dest.setBpmConfBase(bpmConfBaseManager.findOne(bpmConfBaseId));
        bpmProcessManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/bpm/bpm-process-list.do";
    }

    @RequestMapping("bpm-process-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        List<BpmProcess> bpmCategories = bpmProcessManager.findByIds(selectedItem);
		bpmProcessManager.delete(bpmCategories);
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/bpm/bpm-process-list.do";
    }

    @RequestMapping("bpm-process-export")
    public void export(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap,
            HttpServletResponse response) throws Exception {
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        page = bpmProcessManager.pagedQuery(page, propertyFilters);

        List<BpmProcess> bpmCategories = (List<BpmProcess>) page.getResult();
        TableModel tableModel = new TableModel();
        tableModel.setName("bpm-process");
        tableModel.addHeaders("id", "name");
        tableModel.setData(bpmCategories);
        exportor.export(response, tableModel);
    }

    // ~ ======================================================================
    @Resource
    public void setBpmProcessManager(BpmProcessManager bpmProcessManager) {
        this.bpmProcessManager = bpmProcessManager;
    }

    @Resource
    public void setBpmCategoryManager(BpmCategoryManager bpmCategoryManager) {
        this.bpmCategoryManager = bpmCategoryManager;
    }


    @Resource
    public void setBpmConfBaseManager(BpmConfBaseManager bpmConfBaseManager) {
        this.bpmConfBaseManager = bpmConfBaseManager;
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
