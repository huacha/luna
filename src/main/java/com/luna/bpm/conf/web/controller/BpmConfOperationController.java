package com.luna.bpm.conf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.bpm.conf.service.BpmConfOperationService;
import com.luna.common.web.controller.BaseCRUDController;

@Controller
@RequestMapping(value="/bpm/conf/operation")
public class BpmConfOperationController   extends BaseCRUDController<BpmConfOperation, Long> {
    private BpmConfOperationService getBpmConfOperationService() {
        return (BpmConfOperationService) baseService;
    }

	
	public BpmConfOperationController() {
		setResourceIdentity("bpm:conf:operation");
    }

//    @RequestMapping("bpm-conf-operation-list")
//    public String list(@RequestParam("bpmConfNodeId") Long bpmConfNodeId,
//            Model model) {
//        List<String> operations = new ArrayList<String>();
//        operations.add("保存草稿");
//        operations.add("完成任务");
//        operations.add("驳回");
//        operations.add("转办");
//        operations.add("协办");
//
//        BpmConfNode bpmConfNode = bpmConfNodeManager.findOne(bpmConfNodeId);
//        Long bpmConfBaseId = bpmConfNode.getBpmConfBase().getId();
//        List<BpmConfOperation> bpmConfOperations = bpmConfOperationManager.findByBpmConfNode(bpmConfNode);
//
//        for (Iterator<String> iterator = operations.iterator(); iterator
//                .hasNext();) {
//            String value = iterator.next();
//
//            for (BpmConfOperation bpmConfOperation : bpmConfOperations) {
//                if (value.equals(bpmConfOperation.getValue())) {
//                    iterator.remove();
//
//                    break;
//                }
//            }
//        }
//
//        model.addAttribute("bpmConfBaseId", bpmConfBaseId);
//        model.addAttribute("bpmConfOperations", bpmConfOperations);
//        model.addAttribute("operations", operations);
//
//        return "bpm/bpm-conf-operation-list";
//    }

//    @RequestMapping("bpm-conf-operation-save")
//    public String save(@ModelAttribute BpmConfOperation bpmConfOperation,
//            @RequestParam("bpmConfNodeId") Long bpmConfNodeId) {
//        if ((bpmConfOperation.getValue() == null)
//                || "".equals(bpmConfOperation.getValue())) {
//            return "redirect:/bpm/bpm-conf-operation-list.do?bpmConfNodeId="
//                    + bpmConfNodeId;
//        }
//
//        bpmConfOperation.setBpmConfNode(bpmConfNodeManager.findOne(bpmConfNodeId));
//        bpmConfOperationManager.save(bpmConfOperation);
//
//        return "redirect:/bpm/bpm-conf-operation-list.do?bpmConfNodeId="
//                + bpmConfNodeId;
//    }
//

}
