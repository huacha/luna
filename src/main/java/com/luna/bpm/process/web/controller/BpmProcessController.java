package com.luna.bpm.process.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.process.entity.BpmProcess;
import com.luna.bpm.process.service.BpmProcessService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping("/bpm/process/process")
public class BpmProcessController  extends BaseCRUDController<BpmProcess, Long>{
//    private BpmProcessManager bpmProcessManager;
//    private BpmCategoryManager bpmCategoryManager;
//    private BpmConfBaseManager bpmConfBaseManager;
//    private Exportor exportor;
//    private BeanMapper beanMapper = new BeanMapper();
//    private MessageHelper messageHelper;
    private BpmProcessService getBpmProcessService() {
        return (BpmProcessService) baseService;
    }

	public BpmProcessController() {
		setResourceIdentity("bpm:process:process");
    }


	
//    @RequestMapping("bpm-process-export")
//    public void export(@ModelAttribute Page page,
//            @RequestParam Map<String, Object> parameterMap,
//            HttpServletResponse response) throws Exception {
//        List<PropertyFilter> propertyFilters = PropertyFilter
//                .buildFromMap(parameterMap);
//        page = bpmProcessManager.pagedQuery(page, propertyFilters);
//
//        List<BpmProcess> bpmCategories = (List<BpmProcess>) page.getResult();
//        TableModel tableModel = new TableModel();
//        tableModel.setName("bpm-process");
//        tableModel.addHeaders("id", "name");
//        tableModel.setData(bpmCategories);
//        exportor.export(response, tableModel);
//    }

}
