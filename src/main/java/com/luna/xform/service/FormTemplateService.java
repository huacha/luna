package com.luna.xform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.luna.common.service.BaseService;
import com.luna.common.utils.JsonUtil;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.model.FieldModel;
import com.luna.xform.repository.FormTemplateRepository;

@Service
public class FormTemplateService extends BaseService<FormTemplate, Long> {

	FormTemplateRepository getFormTemplateRepository() {
		return (FormTemplateRepository) baseRepository;
	}

	@Override
	public FormTemplate save(FormTemplate m) throws Exception {
		String sql = "";
		sql = this.getCreateSql(m);
		this.jdbcTemplate.execute(sql);
		log.info("创建表： ", sql);
		return super.save(m);
	}

	@Override
	public FormTemplate update(FormTemplate m) throws Exception {
		return super.update(m);
	}

	private String getCreateSql(FormTemplate m) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("create table ");
		String code = m.getCode();// 作为表名
		sql.append(" `").append(code).append("` (`id` bigint not null auto_increment, ");

		String content = m.getContent();// 解析出字段名
		List<FieldModel> list = this.getFieldsFromJson(content);
		for (FieldModel fieldModel : list) {
			String field = fieldModel.getName();
			String type = fieldModel.getType();
			if (type.equals("textarea")) {
				sql.append("`").append(field).append("` varchar(2000),");
			} else if (type.equals("datepicker")) {
				sql.append("`").append(field).append("` timestamp,");
			} else {
				sql.append("`").append(field).append("` varchar(200),");
			}
		}
		sql.append("constraint `pk_").append(code).append("` primary key(`id`))");
		return sql.toString();
	}
	
	public String getSelectSql(Long id){
		FormTemplate template = this.getFormTemplateRepository().findOne(id);
		String tableName = template.getCode();
		StringBuilder sql = new StringBuilder("select * from ").append(tableName);
		log.info(sql.toString());
		return sql.toString();
	}
	
	public String getSelectSql(Long id,Map<String,?> map){
		FormTemplate template = this.getFormTemplateRepository().findOne(id);
		String tableName = template.getCode();
		StringBuilder sql = new StringBuilder("select * from ").append(tableName);
		if(map.size()>0){
			sql.append(" where ");
			Set<String> keys = map.keySet();
			int i = 0;
			for (String key : keys) {
				if(i > 0){
					sql.append(" and ");
				}
				String value = map.get(key).toString();
				sql.append(key).append(" = '").append(value).append("'");
				i++;
			}
		}
		log.info(sql.toString());
		return sql.toString();
	}
	
	public String getSelectSql(Long formid,Long dataid){
		FormTemplate template = this.getFormTemplateRepository().findOne(formid);
		String tableName = template.getCode();
		StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where id = ").append(dataid);
		log.info(sql.toString());
		return sql.toString();
	}
	
	public List<FieldModel> getFields(Long id) throws Exception{
		FormTemplate template = this.getFormTemplateRepository().findOne(id);
		String json = template.getContent();
		return this.getFieldsFromJson(json);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<FieldModel> getFieldsFromJson(String json) throws Exception {
		List<FieldModel> list = new ArrayList<FieldModel>();
		Map map = JsonUtil.toObj(json, Map.class);
		List<Map> sections = (List<Map>) map.get("sections");
		for (Map section : sections) {
			String type = section.get("type").toString();
			if (type.equals("grid")) {
				List<Map> fields = (List<Map>) section.get("fields");
				Map<String,String> titleMap = new HashMap<String, String>();
				for (Map field : fields) {
					String fieldType = field.get("type").toString();
					if (fieldType.equals("label")) {
						String title = field.get("text").toString();
						int row = (Integer)field.get("row");
						int col = (Integer)field.get("col");
						String key = row + "-" + (col + 1);
						titleMap.put(key, title);
					}
				}
				for (Map field : fields) {
					String fieldType = field.get("type").toString();
					if (!fieldType.equals("label")) {
						String fieldName = field.get("name").toString();
						int row = (Integer)field.get("row");
						int col = (Integer)field.get("col");
						String key = row + "-" + col;
						String title = titleMap.get(key);
						FieldModel model = new FieldModel();
						model.setName(fieldName);
						model.setType(fieldType);
						model.setTitle(title);
						list.add(model);
					}
				}
				break;
			}
		}
		return list;
	}
	
	public FormTemplate getFormTemplate(Long formid) {
		FormTemplate template = this.getFormTemplateRepository().findOne(formid);
		return template;
	}

	public String getInsertSql(Long formid) throws Exception {
		FormTemplate template = this.getFormTemplateRepository().findOne(formid);
		String content = template.getContent();// 解析出字段名
		List<FieldModel> list = this.getFieldsFromJson(content);
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		int i = 0;
		for (FieldModel fieldModel : list) {
			String field = fieldModel.getName();
			if(i > 0){
				fields.append(" , ");
				values.append(" , ");
			}
			fields.append(field);
			values.append(":").append(field);
			i++;
		}
		String tableName = template.getCode();
		StringBuilder sql = new StringBuilder("insert into ").append(tableName);
		sql.append(" (").append(fields).append(") values (").append(values).append(") ");
		log.info(sql.toString());
		return sql.toString();
	}
	
	public String getUpdateSql(Long formid) throws Exception {
		FormTemplate template = this.getFormTemplateRepository().findOne(formid);
		String content = template.getContent();// 解析出字段名
		List<FieldModel> list = this.getFieldsFromJson(content);
		StringBuilder fields = new StringBuilder();
		int i = 0;
		for (FieldModel fieldModel : list) {
			String field = fieldModel.getName();
			if(i > 0){
				fields.append(" , ");
			}
			fields.append(field).append(" = ").append(":").append(field);
			i++;
		}
		String tableName = template.getCode();
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
		sql.append(fields).append(" where id = :id");
		log.info(sql.toString());
		return sql.toString();
	}
	
	public String getDeleteSql(Long formid,Long[] ids) {
		String sids = StringUtils.join(ids, ",");
		FormTemplate template = this.getFormTemplateRepository().findOne(formid);
		String tableName = template.getCode();
		StringBuilder sql = new StringBuilder("delete from ").append(tableName).append(" where id in (").append(sids).append(")");
		log.info(sql.toString());
		return sql.toString();
	}

}
