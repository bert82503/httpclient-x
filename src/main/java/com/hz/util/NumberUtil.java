/*
 * Copyright (c)
 */
package com.hz.util;

import java.math.BigDecimal;

/**
 * 获取字符串格式的8位随机数，作为参数r。<br>
 * r,s,p三个参数用于获取pcid（pcid用于获取微博验证码）
 * 
 * @author huagang.li
 * @since 1.0
 */
public class NumberUtil {
	public static String getMathRandomNum() {
		return new BigDecimal(Math.floor(Math.random() * 1e8)).toString();
	}
}
