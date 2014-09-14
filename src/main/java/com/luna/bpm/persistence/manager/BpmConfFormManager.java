package com.luna.bpm.persistence.manager;



import com.luna.bpm.persistence.domain.BpmConfForm;
import com.luna.bpm.persistence.domain.BpmConfNode;
import com.luna.common.repository.BaseRepository;


public interface BpmConfFormManager extends BaseRepository<BpmConfForm,Long> {
	BpmConfForm findByBpmConfNode(BpmConfNode bpmConfNode);
}
