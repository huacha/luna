package com.luna.xform.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FormProcessRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Deprecated
	public Long getStartFormId(Long processId) {
		String sql ="select f.FORM_ID from BPM_CONF_NODE n,BPM_PROCESS p,BPM_CONF_FORM f where p.CONF_BASE_ID = n.CONF_BASE_ID and n.TYPE = 'process' and f.NODE_ID = n.ID and p.ID = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, processId);
		if(list.size() > 0)
			return (Long)list.get(0).get("FORM_ID");
		return null;
	}
	
	public Long getTaskFormId(String processDefinitionId, String taskDefinitionKey) {
		String sql ="select f.FORM_ID from BPM_CONF_NODE n,BPM_CONF_BASE b,BPM_CONF_FORM f where  n.CONF_BASE_ID = b.ID and f.NODE_ID = n.ID and b.PROCESS_DEFINITION_ID = ? and n.CODE = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, processDefinitionId, taskDefinitionKey);
		if(list.size() > 0)
			return (Long)list.get(0).get("FORM_ID");
		return null;
	}
	
	public String getProcessName(String processDefinitionId) {
		String sql ="select p.NAME from BPM_CONF_BASE b,BPM_PROCESS p where  p.CONF_BASE_ID = b.ID and b.PROCESS_DEFINITION_ID = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, processDefinitionId);
		if(list.size() > 0)
			return (String)list.get(0).get("NAME");
		return null;
	}
	
}
