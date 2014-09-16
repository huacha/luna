package com.luna.bpm.conf.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.bpm.conf.entity.BpmConfUser;
import com.luna.common.repository.BaseRepository;


public interface BpmConfUserManager extends BaseRepository<BpmConfUser,Long> {
	@Query("from BpmConfUser where value=?1 and type=?2 and priority=?3 and status=0 and bpmConfNode=?4")
	BpmConfUser findUnique(String value,int type, int priority,BpmConfNode bpmConfNode);
	        
	@Query("from BpmConfUser where bpmConfNode.bpmConfBase.processDefinitionId=?1 and bpmConfNode.code=?2")
	List<BpmConfUser> find(String processDefinitionId,String nodeCode);
	
	List<BpmConfUser> findByBpmConfNode(BpmConfNode bpmConfNode);
}