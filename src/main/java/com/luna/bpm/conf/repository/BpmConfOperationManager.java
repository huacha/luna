package com.luna.bpm.conf.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfOperation;
import com.luna.common.repository.BaseRepository;


public interface BpmConfOperationManager extends
        BaseRepository<BpmConfOperation,Long> {

	@Query("from BpmConfOperation where value=?1 and bpmConfNode=?2")
	BpmConfOperation findUnique(String value, BpmConfNode bpmConfNode);
	
	List<BpmConfOperation> findByBpmConfNode(BpmConfNode bpmConfNode);
}
