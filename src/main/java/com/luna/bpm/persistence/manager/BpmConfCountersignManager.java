package com.luna.bpm.persistence.manager;



import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.persistence.domain.BpmConfCountersign;
import com.luna.common.repository.BaseRepository;


public interface BpmConfCountersignManager extends BaseRepository<BpmConfCountersign,Long> {
	@Query("from BpmConfCountersign where bpmConfNode.bpmConfBase.processDefinitionId=?1 and bpmConfNode.code=?2")
	BpmConfCountersign findUnique(String processDefinitionId,String activityId);
}
