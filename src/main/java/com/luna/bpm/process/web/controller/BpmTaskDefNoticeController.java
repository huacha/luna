package com.luna.bpm.process.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.process.entity.BpmTaskDefNotice;
import com.luna.bpm.process.service.BpmTaskDefNoticeService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping("/bpm/process/taskdefnotice")
public class BpmTaskDefNoticeController extends BaseCRUDController<BpmTaskDefNotice, Long>{
	
    private BpmTaskDefNoticeService getBpmTaskDefNoticeService() {
        return (BpmTaskDefNoticeService) baseService;
    }

	public BpmTaskDefNoticeController() {
		setResourceIdentity("bpm:process:taskdefnotice");
    }
//
//    @RequestMapping("bpm-task-def-notice-export")
//    public void export(@ModelAttribute Page page,
//            @RequestParam Map<String, Object> parameterMap,
//            HttpServletResponse response) throws Exception {
//        List<PropertyFilter> propertyFilters = PropertyFilter
//                .buildFromMap(parameterMap);
//        page = bpmTaskDefNoticeManager.pagedQuery(page, propertyFilters);
//
//        List<BpmTaskDefNotice> bpmCategories = (List<BpmTaskDefNotice>) page
//                .getResult();
//        TableModel tableModel = new TableModel();
//        tableModel.setName("bpm-process");
//        tableModel.addHeaders("id", "name");
//        tableModel.setData(bpmCategories);
//        exportor.export(response, tableModel);
//    }
//
//    @RequestMapping("bpm-task-def-notice-removeNotice")
//    public String removeNotice(@RequestParam("id") Long id) {
//        BpmTaskDefNotice bpmTaskDefNotice = bpmTaskDefNoticeManager.findOne(id);
//        Long bpmProcessId = bpmTaskDefNotice.getBpmProcess().getId();
//        bpmTaskDefNoticeManager.delete(bpmTaskDefNotice);
//
//        return "redirect:/bpm/bpm-task-def-notice-list.do?bpmProcessId="
//                + bpmProcessId;
//    }

}
