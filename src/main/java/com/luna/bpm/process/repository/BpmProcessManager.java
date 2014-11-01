package com.luna.bpm.process.repository;

import com.luna.bpm.conf.entity.BpmConfBase;
import com.luna.bpm.process.entity.BpmProcess;
import com.luna.common.repository.BaseRepository;

public interface BpmProcessManager extends BaseRepository<BpmProcess, Long> {

	BpmProcess findByName(String fieldValue);
	
	BpmProcess findByBpmConfBase(BpmConfBase bpmConfBase);
}
