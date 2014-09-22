package com.luna.maintain.extkeyvalue.entity;

/**
 * keyvalue数据类型
 * 
 * <p>
 * Date: 13-2-19 下午2:30
 * <p>
 * Version: 1.0
 */
public enum ExtKeyValueType {
	string("静态字符串"), sql("数据表");

	private final String info;

	private ExtKeyValueType(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	// ("static","静态字符串"), type2("sql","数据表");
	// private final String value;
	// private final String info;
	// private ExtKeyValueType(String value, String info) {
	// this.value = value;
	// this.info = info;
	// }
	//
	// public String getInfo() {
	// return info;
	// }
	//
	// public String getValue() {
	// return value;
	// }

}
