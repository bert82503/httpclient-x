/*
 * Copyright (c)
 */
package com.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.http.HttpConfigUtils;
import com.http.HttpWorkerPool;
import com.service.TaobaoService;

/**
 * Class description goes here. (A brief description of the purpose.)
 * 
 * @author Daqiang.Yan
 * @since 1.0
 */
public class TaobaoServiceImpl implements TaobaoService {

	private final HttpWorkerPool httpWorker;

	public TaobaoServiceImpl() {
		httpWorker = HttpWorkerPool.getInstance();
	}

	@Override
	public String register(String username, String password, String country) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 从微博登录成功后的响应信息中解析出淘宝的注册url
	 * 
	 * @param response
	 *            微博登录成功后的响应：<br>
	 *            <html><head><script language=
	 *            'javascript'>parent.sinaSSOController.feedBackUrlCallBack({"result":true,"userinfo":{"uniqueid":"5356920239","userid":null,"displayname":null,"userdomain":"?wvr=5&lf=reg"},"redirect":"https:\/\/login.taobao.com\/aso\/tvs?domain=weibo&sid=58fc731a6696f759a7730ca5da772909&target=687474703A2F2F7777772E74616F62616F2E636F6D2F&wbp=E6ibDMkxfMm4G%2BVoSQc%2FA%2Buc4hsSQnRa2P91S%2BavooTUN9lSpKUyH6%2BK0mfgOILPTRmz8bfenzNaeV35EOR5epH4%2FxzXPEz%2BN7kbU%2FgXBuNAmQdGrKBHY%2F81U8X8%2F9VxsfjSz2RBcudAcnYtwc5qz%2FTTUBZhwNI6MiRSgV%2F1XL0Tab6L1LiDdpl6&sign=MCwCFCfyYWND6J%2BIEEv6aiBA7PO%2BeYv1AhQDCduxd58giLN8Z0rCyYkt%2FL2L0w%3D%3D"});</script></head><body></body></htm
	 *            l >
	 * @return
	 */
	private String getCallBackFromWeiboLoginResponse(String response) {
		Document doc = Jsoup.parse(response);
		Element javascript = doc.getElementsByAttributeValue("language",
				"javascript").first();
		return javascript.data();
	}

	/**
	 * 从callback信息中解析出重定向的URL
	 * 
	 * @param response
	 *            微博登录成功后的响应：<br>
	 * @return
	 */
	private String parseRedirectUrlFromCallBack(String callBack) {
		String jsonObjStr = StringUtils.substringAfter(callBack,
				"feedBackUrlCallBack(");
		jsonObjStr = StringUtils.substringBefore(jsonObjStr, ");");
		JSONObject jsonObj = JSON.parseObject(jsonObjStr);
		return jsonObj.getString("redirect");
	}

	/**
	 * 获取注册时的验证码url
	 * http://pin.aliyun.com//get_img?identity=member1.taobao.com&sessionid=
	 * ALI83e296c78bb312fca11496bbb37d37e9&kjtype=default&t=1419864028668
	 * 
	 * @param identity
	 * @param kjtype
	 * @param sessionid
	 * @param t
	 * @return
	 */
	private String getRegisterVerifycodeUrl(String identity,
			String kjtype, String sessionid, String t) {
		return new StringBuilder()
				.append(HttpConfigUtils.getTaobaoRegisterVerifycodeUrl())
				.append("?identity=").append(identity).append("&sessionid=")
				.append(sessionid).append("&kjtype=")
				.append(kjtype).append("&t=").append(t).toString();
	}

	/**
	 * 调用手机打码接口，获取手机验证码
	 * 
	 * @return
	 */
	private String getPhoneCode() {
		// TODO
		return null;
	}

}
