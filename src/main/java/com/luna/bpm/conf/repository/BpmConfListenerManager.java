package com.luna.bpm.conf.repository;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfListener;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.common.repository.BaseRepository;

public interface BpmConfListenerManager extends
		BaseRepository<BpmConfListener, Long> {
	@Query("from BpmConfListener where value=?1 and type=?2 and status=0 and bpmConfNode=?3")
	public BpmConfListener findUnique(String value, int type,
			BpmConfNode bpmConfNode);
}
