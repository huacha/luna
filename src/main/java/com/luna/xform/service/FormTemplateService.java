package com.luna.xform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.luna.common.service.BaseService;
import com.luna.common.utils.JsonUtil;
import com.luna.xform.entity.FormTemplate;
import com.luna.xform.model.FieldModel;
import com.luna.xform.repository.FormTemplateRepository;

@Service
public class FormTemplateService extends BaseService<FormTemplate, Long> {

	private static Logger log = LoggerFactory.getLogger(FormTemplateService.class);

	FormTemplateRepository getFormTemplateRepository() {
		return (FormTemplateRepository) baseRepository;
	}

	@Override
	public FormTemplate save(FormTemplate m) throws Exception {
		String sql = "";
		sql = this.generateCreateSql(m);
		this.jdbcTemplate.execute(sql);
		log.info("创建表： ", sql);
		return super.save(m);
	}

	@Override
	public FormTemplate update(FormTemplate m) throws Exception {
		return super.update(m);
	}

	private String generateCreateSql(FormTemplate m) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("create table ");
		String code = m.getCode();// 作为表名
		sql.append(" `").append(code)
				.append("` (`id` bigint not null auto_increment, ");

		String content = m.getContent();// 解析出字段名
		List<FieldModel> list = this.parseJson(content);
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

		sql.append("constraint `pk_").append(code)
				.append("` primary key(`id`))");
		return sql.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<FieldModel> parseJson(String json) throws Exception {
		List<FieldModel> list = new ArrayList<FieldModel>();
		Map map = JsonUtil.toObj(json, Map.class);
		List<Map> sections = (List<Map>) map.get("sections");
		for (Map section : sections) {
			String type = section.get("type").toString();
			if (type.equals("grid")) {
				List<Map> fields = (List<Map>) section.get("fields");
				for (Map field : fields) {
					String fieldType = field.get("type").toString();
					if (!fieldType.equals("label")) {
						String fieldName = field.get("name").toString();
						FieldModel model = new FieldModel();
						model.setName(fieldName);
						model.setType(fieldType);
						list.add(model);
					}
				}
				break;
			}
		}
		return list;
	}

}
