package com.luna.xform.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FormProcessRepository {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Long getFormId(Long processId) {
		String sql ="select f.FORM_ID from BPM_CONF_NODE n,BPM_PROCESS p,BPM_CONF_FORM f where p.CONF_BASE_ID = n.CONF_BASE_ID and n.TYPE = 'process' and f.NODE_ID = n.ID and p.ID = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, processId);
		if(list.size() > 0)
			return (Long)list.get(0).get("FORM_ID");
		return null;
	}
}
