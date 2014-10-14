package com.luna.bpm.conf.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.luna.bpm.conf.entity.BpmConfForm;
import com.luna.bpm.conf.entity.BpmConfNode;
import com.luna.common.repository.BaseRepository;
import com.luna.xform.entity.FormTemplate;


public interface BpmConfFormManager extends BaseRepository<BpmConfForm,Long> {

	@Query("from BpmConfForm where formTemplate=?1 and bpmConfNode=?2")
	BpmConfForm findUnique(FormTemplate formTemplate, BpmConfNode bpmConfNode);
	
	List<BpmConfForm> findByBpmConfNode(BpmConfNode bpmConfNode);
}
