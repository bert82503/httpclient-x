/*
 * Copyright (c)
 */
package com.http;

/**
 * HTTP 工具类。
 * 
 * @author huagang.li
 * @since 1.0
 */
public class HttpUtils {

	/**
	 * 获取给定URI的主机名。
	 * 
	 * @param uri
	 * @return
	 */
	public static String getHostname(String uri) {
		String[] result = uri.split("[/?]", 4);
		return result[2];
	}

}
