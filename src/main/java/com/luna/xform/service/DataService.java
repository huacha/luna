package com.luna.xform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.luna.common.repository.Pagination;
import com.luna.xform.model.FieldModel;
import com.luna.xform.model.TaskData;
import com.luna.xform.repository.FormProcessRepository;

@Service
public class DataService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	FormProcessRepository formProcessRepository;
	@Autowired
	FormTemplateService formTemplateService;
	
	/**
	 * 保存任务与业务数据的关系
	 * 
	 * @param processInstanceId
	 * @param taskId
	 * @param tableName
	 * @param bussid
	 */
	public int saveTaskBussiness(String processInstanceId,String taskId,String taskName,String tableName,long bussid) {
		String sql = "insert into BPM_TASK_BUSSINESS(PROCESSINST_ID,TASK_ID,TASK_NAME,TABLE_NAME,BUSSI_ID) values (?,?,?,?,?)";
		return jdbcTemplate.update(sql, processInstanceId, taskId,taskName, tableName, bussid);
	}
	
	/**
	 * 查询流程相关的业务数据
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public List<Map<String,Object>> findProcessBussinessData(String processInstanceId) {
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		String sql = "select * from BPM_TASK_BUSSINESS where PROCESSINST_ID = ?";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, processInstanceId);
		for (Map<String, Object> map : list) {
			String tableName = map.get("TABLE_NAME").toString();
			long bid = (Long)map.get("BUSSI_ID");
			String querySql = "select * from ? where id = ?";
			Map<String, Object> data = jdbcTemplate.queryForMap(querySql, tableName, bid);
			datas.add(data);
		}
		return datas;
	}
	
	/**
	 * 查询任务相关的业务数据
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> findTaskBussinessData(String taskId) {
		String sql = "select * from BPM_TASK_BUSSINESS where TASK_ID = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, taskId);
		if (list.size() == 0) {
			return null;
		}
		Map<String, Object> map = list.get(0);
		String tableName = map.get("TABLE_NAME").toString();
		long bid = (Long)map.get("BUSSI_ID");
		String querySql = "select * from "+tableName+" where id = ?";
		Map<String, Object> data = jdbcTemplate.queryForMap(querySql, bid);
		return data;
	}
	
	/**
	 * 根据任务对应的表单转换业务数据（把key从字段名换成显示名）
	 * 
	 * @param processDefinitionId
	 * @param taskDefinitionKey
	 * @param taskBussinessData
	 * @return
	 * @throws Exception
	 */
	public List<TaskData> translateTaskBussinessData(
			String processDefinitionId, String taskDefinitionKey,
			Map<String, Object> taskBussinessData) throws Exception {
		Long formId = formProcessRepository.getTaskFormId(processDefinitionId,
				taskDefinitionKey);
		if (formId == null) {
			return null;
		}
		List<TaskData> ls = new ArrayList<TaskData>();
		List<FieldModel> fields = formTemplateService.getFields(formId);
		for (FieldModel fieldModel : fields) {
			String name = fieldModel.getName();
			String title = fieldModel.getTitle();
			String val = "";
			if (taskBussinessData != null && taskBussinessData.get(name) != null) {
				val = taskBussinessData.get(name).toString();
			}
			TaskData taskData = new TaskData();
			taskData.setTitle(title);
			taskData.setValue(val);
			ls.add(taskData);
		}
		return ls;
	}

	/**
	 * 保存业务数据，并获取业务数据id
	 * 
	 * @param sql
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public long saveAndGetID(String sql,Map<String,?> map) throws Exception {
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramSource = new MapSqlParameterSource(map);
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		nt.update(sql, paramSource, generatedKeyHolder);
		return generatedKeyHolder.getKey().longValue();
	}
	
	public int save(String sql,Map<String,?> map) throws Exception {
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		return nt.update(sql, map);
	}

	public int update(String sql,Map<String,?> map) throws Exception {
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		return nt.update(sql, map);
	}

	public List<Map<String, Object>> findAll(String sql) {
		return jdbcTemplate.queryForList(sql);
	}
	
	public List<Map<String, Object>> findAll(String sql,Map<String,?> map) {
		NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(jdbcTemplate);
		return nt.queryForList(sql, map);
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
