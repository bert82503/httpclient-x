/*
 * Copyright (c)
 */
package com.hz.util;

import static java.lang.System.out;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Class description goes here. (A brief description of the purpose.)
 * 
 * 
 * @author huagang.li
 * @since 1.0
 */
public class NumberUtilTest {
	@Test()
	public void getMathRandomNum() {
		out.println(NumberUtil.getWeiboMathRandomNum());
		assertEquals(NumberUtil.getWeiboMathRandomNum().length(), 8);
	}

}
