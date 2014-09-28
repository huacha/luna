package com.luna.xform.model;

import org.apache.commons.lang3.StringUtils;


public class FieldModel {
    private String name;
    private String type;
    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getTitle() {
		if(StringUtils.isBlank(title))
			title = name;
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
