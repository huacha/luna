package com.luna.bpm.process.repository;



import java.util.List;

import com.luna.bpm.process.entity.BpmDelegateInfo;
import com.luna.common.repository.BaseRepository;


public interface BpmDelegateInfoManager extends BaseRepository<BpmDelegateInfo,Long> {
	List<BpmDelegateInfo> findByAssignee(String userId);
}
