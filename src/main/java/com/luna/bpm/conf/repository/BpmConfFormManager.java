package com.luna.bpm.conf.repository;



import java.util.List;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.common.repository.BaseRepository;


public interface BpmConfFormManager extends BaseRepository<BpmConfForm,Long> {
	List<BpmConfForm> findByBpmConfNode(BpmConfNode bpmConfNode);
}
