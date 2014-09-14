package com.luna.bpm.persistence.manager;



import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.persistence.domain.BpmConfBase;
import com.luna.bpm.persistence.domain.BpmConfNode;
import com.luna.common.repository.BaseRepository;


public interface BpmConfNodeManager extends BaseRepository<BpmConfNode,Long> {
	@Query("from BpmConfNode where code=?1 and bpmConfBase=?2")
	BpmConfNode findUnique(String processId,BpmConfBase bpmConfBase);
}
