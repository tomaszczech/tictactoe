package com.czecht.tictactoe.ddd.presentation;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.transform.ResultTransformer;
import org.joda.time.DateTime;

public abstract class AbstractSqlResultTransformer<T> implements ResultTransformer {

	private Object[] objects;

	private String[] strings;

	private void initialize(Object[] objects, String[] strings) {
		this.objects = objects;
		this.strings = strings;
	}

	@Override
	public Object transformTuple(Object[] objects, String[] strings) {
		initialize(objects, strings);

		return transformObject();
	}

	protected abstract T transformObject();

	protected String getString(String key) {
		return (String)getObject(key);
	}

	protected Boolean getBoolean(String key) {
		return (Boolean)getObject(key);
	}

	protected Long getLong(String key) {
		return (Long)getObject(key);
	}

	protected BigInteger getBigInteger(String key) {
		return (BigInteger)getObject(key);
	}

	protected Integer getInteger(String key) {
		return (Integer)getObject(key);
	}

	protected DateTime getDateTime(String key) {
		return (DateTime)getObject(key);
	}

	protected Object getObject(String key) {
		int index = ArrayUtils.indexOf(strings, key);
		return objects[index];
	}
}
