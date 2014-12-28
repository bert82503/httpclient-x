/*
 * Copyright (c)
 */
package com.hz.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Class description goes here. (A brief description of the purpose.)
 * 
 * @author Daqiang.Yan
 * @since 1.0
 */
public class JsUtil {
	private static ScriptEngineManager sem = null;
	private static ScriptEngine se = null;
	private static String rsaPubkey = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D245A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD3993CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";
	private static JsUtil jsUtil = new JsUtil();

	static {
		sem = new ScriptEngineManager();
		se = sem.getEngineByName("javascript");
	}

	private JsUtil() {
	}

	public static JsUtil getInstance() {
		return jsUtil;
	}

	public void eval(String jsString) {
		try {
			se.eval(jsString);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 传入图片Url，调用联众打码接口，获取验证码识别文字。<br/>
	 * 
	 * @param pwd
	 *            ：密码
	 * @param servicetime
	 *            ：系统时间
	 * @param nonce
	 *            ：随机数生成
	 * @return：验证码文字
	 */
	public static String getSpParamOfWeiboLogin(String pwd, String servicetime,
			String nonce) {
		return getSpParamOfWeiboLogin(pwd, servicetime, nonce, rsaPubkey);
	}

	/**
	 * 传入图片Url，调用联众打码接口，获取验证码识别文字。<br/>
	 * 
	 * @param pwd
	 *            ：密码
	 * @param servicetime
	 *            ：系统时间
	 * @param nonce
	 *            ：随机数生成
	 * @param rsaPubkey
	 *            ：一般固定
	 * @return：验证码文字
	 */
	public static String getSpParamOfWeiboLogin(String pwd, String servicetime,
			String nonce, String pubkey) {
		if (pubkey == null || "".equals(pubkey)) {
			pubkey = rsaPubkey;
		}
		String sp = null;
		try {
			if (se instanceof Invocable) {
				Invocable invoke = (Invocable) se;
				sp = invoke.invokeFunction("getpass", pwd, servicetime, nonce,
						pubkey).toString();//
				System.out.println("param sp: " + sp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sp;
	}
}
