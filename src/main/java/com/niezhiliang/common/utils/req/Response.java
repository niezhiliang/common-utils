package com.niezhiliang.common.utils.req;


import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Response {

	private Map<String, String> headers;

	private int code;

	private byte[] body;

	public Response(int code, byte[] body) {
		this.code = code;
		this.body = body;
	}

	public Response(int code, byte[] body, Map<String, String> headers) {
		this.code = code;
		this.body = body;
		this.headers = headers;
	}

	public int getCode() {
		return code;
	}

	public byte[] getBodyAsBytes() {
		return body;
	}

	public String getBody() {
		try {
			return new String(body, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getText() {
		return getBody();
	}

	public <T> T toBean(Class<T> cls) {
		return JSON.parseObject(getBody(), cls);
	}

	public Map<String, Object> toMap() {
		return JSON.parseObject(getBody(), Map.class);
	}

	public boolean isSuccess() {
		return code >= 200 && code < 300;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.code);
		str.append("\n");
		str.append(getBody());
		return str.toString();
	}
}
