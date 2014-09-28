package com.luna.xform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.luna.common.repository.Pagination;

@Service
public class DataService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	
	public int save(String sql,Map<String,String> map) throws Exception {
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		return nt.update(sql, map);
	}

	public int update(String sql,Map<String,String> map) throws Exception {
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		return nt.update(sql, map);
	}

	public List<Map<String, Object>> findAll(String sql) {
		return jdbcTemplate.queryForList(sql);
	}
	
	public Pagination find(String sql, int currentPage, int numPerPage) {
		Pagination page = new Pagination(sql, currentPage, numPerPage, jdbcTemplate);
		return page;
	}
	
	public Map<String,Object> findOne(String sql,Long id) {
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		return nt.queryForMap(sql, paramMap);
	}
	
	public Map<String,Object> findOne(String sql) {
		return jdbcTemplate.queryForMap(sql);
	}
	
	public int delete(String sql) {
		return jdbcTemplate.update(sql);
	}

}
