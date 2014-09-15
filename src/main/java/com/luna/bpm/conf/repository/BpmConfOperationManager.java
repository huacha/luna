package com.luna.bpm.conf.repository;



import java.util.List;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.common.repository.BaseRepository;


public interface BpmConfOperationManager extends
        BaseRepository<BpmConfOperation,Long> {
	
	List<BpmConfOperation> findByBpmConfNode(BpmConfNode bpmConfNode);
}
