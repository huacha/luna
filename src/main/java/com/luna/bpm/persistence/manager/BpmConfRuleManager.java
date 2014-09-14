package com.luna.bpm.persistence.manager;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.persistence.domain.BpmConfRule;
import com.luna.common.repository.BaseRepository;


public interface BpmConfRuleManager extends BaseRepository<BpmConfRule,Long> {
	@Query("from BpmConfRule where bpmConfNode.bpmConfBase.processDefinitionId=? and bpmConfNode.code=?")
	List<BpmConfRule> find(String processDefinitionId,String taskDefinitionKey);
}
