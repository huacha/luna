package com.luna.bpm.conf.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfNotice;
import com.luna.common.repository.BaseRepository;


public interface BpmConfNoticeManager extends BaseRepository<BpmConfNotice,Long> {
	@Query("from BpmConfNotice where bpmConfNode.bpmConfBase.processDefinitionId=?1 and bpmConfNode.code=?2")
	List<BpmConfNotice> find(String processDefinitionId,String taskDefinitionKey);
}
