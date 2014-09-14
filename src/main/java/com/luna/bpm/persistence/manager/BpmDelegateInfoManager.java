package com.luna.bpm.persistence.manager;



import java.util.List;

import com.luna.bpm.persistence.domain.BpmDelegateInfo;
import com.luna.common.repository.BaseRepository;


public interface BpmDelegateInfoManager extends BaseRepository<BpmDelegateInfo,Long> {
	List<BpmDelegateInfo> findByAssignee(String userId);
}
