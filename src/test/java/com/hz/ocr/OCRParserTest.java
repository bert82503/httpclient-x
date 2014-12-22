/*
 * Copyright (c)
 */
package com.hz.ocr;

import static java.lang.System.out;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Class description goes here. (A brief description of the purpose.)
 * 
 *
 * @author huagang.li
 * @since 1.0
 */
public class OCRParserTest {
	
	@Test(dataProvider = "parseOCR")
	public void parseOCR(String imgUrl, String expected) {
		out.println(OCRParser.parseOCR(imgUrl));
		assertEquals(OCRParser.parseOCR(imgUrl).length(), 5);
	}

	@DataProvider(name = "parseOCR")
	protected static final Object[][] parseOCRTestData() {
		Object[][] testData = new Object[][] {
 {
				"http://login.sina.com.cn/cgi/pin.php?r=73973473&amp;s=0&amp;p=hk-33e35923404e8af9361901f20cd3ec138628&qq-pf-to=pcqq.c2c",
						"" },
				{
						"http://login.sina.com.cn/cgi/pin.php?r=73973473&amp;s=0&amp;p=hk-33e35923404e8af9361901f20cd3ec138628&qq-pf-to=pcqq.c2c",
						"" }
		};
		return testData;
	}
	

	@Test(dataProvider = "getTextOfOCRResult")
	public void getTextOfOCRResult(String result, String expected) {
		// assertEquals(OCRParser.getTextOfOCRResult(result), expected);
	}

	@DataProvider(name = "getTextOfOCRResult")
	protected static final Object[][] getTextOfOCRResultTestData() {
		Object[][] testData = new Object[][] {
 { "Y7SSA|!|332082732&worker",
				"Y7SSA" },
				{ "QZUUS|!|332118413&worker", "QZUUS" },
				{ "332118413&worker", null },
 { "", null },
 { null, null },

		};
		return testData;
	}
}
