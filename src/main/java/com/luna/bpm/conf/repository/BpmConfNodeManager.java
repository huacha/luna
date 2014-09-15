package com.luna.bpm.conf.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.common.repository.BaseRepository;


public interface BpmConfNodeManager extends BaseRepository<BpmConfNode,Long> {
	@Query("from BpmConfNode where code=?1 and bpmConfBase=?2")
	BpmConfNode findUnique(String processId,BpmConfBase bpmConfBase);
	
	List<BpmConfNode> findByBpmConfBase(BpmConfBase bpmConfBase);
}
