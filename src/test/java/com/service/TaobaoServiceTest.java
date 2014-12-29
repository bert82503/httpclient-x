/*
 * Copyright (c)
 */
package com.service;

import static java.lang.System.out;
import static org.testng.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.http.HttpConfigUtils;
import com.service.impl.TaobaoServiceImpl;

/**
 * Class description goes here. (A brief description of the purpose.)
 * 
 * @author Daqiang.Yan
 * @since 1.0
 */
public class TaobaoServiceTest {
	private TaobaoServiceImpl taobaoService;

	@BeforeClass
	public void init() {
		taobaoService = new TaobaoServiceImpl();
	}

	@Test(dataProvider = "getTaobaoRegisterVerifycodeUrl")
	public void getTaobaoRegisterVerifycodeUrl(String identity, String kjtype,
			String sessionid, String t, String result) {
		assertEquals(
				new StringBuilder()
						.append(HttpConfigUtils
								.getTaobaoRegisterVerifycodeUrl())
						.append("?identity=").append(identity)
						.append("&sessionid=").append(sessionid)
						.append("&kjtype=").append(kjtype).append("&t=")
						.append(t).toString(), result);
	}

	@DataProvider(name = "getTaobaoRegisterVerifycodeUrl")
	protected static final Object[][] getTaobaoRegisterVerifycodeUrlTestData() {
		Object[][] testData = new Object[][] {
 { "member1.taobao.com",
				"default", "ALI83e296c78bb312fca11496bbb37d37e9",
						"1419864028668",
						"http://pin.aliyun.com//get_img?identity=member1.taobao.com&sessionid=ALI83e296c78bb312fca11496bbb37d37e9&kjtype=default&t=1419864028668" },
		};
		return testData;
	}

	@Test
	public void parseTaobaoRegisterUrlFromWeiboLoginResponse() {
		String response = "<html><head><script language='javascript'>parent.sinaSSOController.feedBackUrlCallBack({\"result\":true,\"userinfo\":{\"uniqueid\":\"5356920239\",\"userid\":null,\"displayname\":null,\"userdomain\":\"?wvr=5&lf=reg\"},\"redirect\":\"https:\\/\\/login.taobao.com\\/aso\\/tvs?domain=weibo&sid=58fc731a6696f759a7730ca5da772909&target=687474703A2F2F7777772E74616F62616F2E636F6D2F&wbp=E6ibDMkxfMm4G%2BVoSQc%2FA%2Buc4hsSQnRa2P91S%2BavooTUN9lSpKUyH6%2BK0mfgOILPTRmz8bfenzNaeV35EOR5epH4%2FxzXPEz%2BN7kbU%2FgXBuNAmQdGrKBHY%2F81U8X8%2F9VxsfjSz2RBcudAcnYtwc5qz%2FTTUBZhwNI6MiRSgV%2F1XL0Tab6L1LiDdpl6&sign=MCwCFCfyYWND6J%2BIEEv6aiBA7PO%2BeYv1AhQDCduxd58giLN8Z0rCyYkt%2FL2L0w%3D%3D\"});</script></head><body></body></html>";
		Document doc = Jsoup.parse(response);
		Element javascript = doc.getElementsByAttributeValue("language",
				"javascript").first();
		out.println(javascript.data());
	}

}
