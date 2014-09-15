package com.luna.bpm.conf.repository;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.common.repository.BaseRepository;


public interface BpmConfBaseManager extends BaseRepository<BpmConfBase,Long> {
	@Query("from BpmConfBase where processDefinitionKey=?1 and processDefinitionVersion=?2")
	BpmConfBase findUnique(String processDefinitionKey,int processDefinitionVersion);
}
