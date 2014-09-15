package com.luna.bpm.conf.repository;



import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.common.repository.BaseRepository;


public interface BpmConfFormManager extends BaseRepository<BpmConfForm,Long> {
	BpmConfForm findByBpmConfNode(BpmConfNode bpmConfNode);
}
