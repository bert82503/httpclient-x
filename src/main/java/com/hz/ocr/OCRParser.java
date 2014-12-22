package com.hz.ocr;

import java.io.File;

import com.http.HttpConfigUtils;
import com.http.HttpWorkerPool;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;

public class OCRParser {
	private static final String split = "|";
	private static String DLLPATH = new File(OCRParser.class.getClass()
			.getResource("/").getPath()).getPath()
			+ "\\dll\\FastVerCode.dll";

	public interface FastVerCode extends Library {
		FastVerCode INSTANCE = (FastVerCode) Native.loadLibrary(DLLPATH,
				FastVerCode.class);

		public String GetUserInfo(String UserName, String passWord);

		public String RecByte(byte[] imgByte, int len, String username,
				String password);

		public String RecYZM(String path, String UserName, String passWord);

		public void ReportError(WString UserName, WString passWord);

		public int Reglz(String userName, String passWord, String email,
				String qq, String dlId, String dlAccount);

	}

	/**
	 * 传入图片Url，调用联众打码接口，获取验证码识别文字。<br/>
	 * 
	 * @param imgUrl
	 *            ：图片Url
	 * @return：验证码文字
	 */
	public static String parseOCR(String imgUrl) {
		try {
			byte[] bytes = HttpWorkerPool.getInstance().getByteArray(imgUrl);
			String result = FastVerCode.INSTANCE.RecByte(bytes, bytes.length,
					HttpConfigUtils.getLianZhongUser(),
					HttpConfigUtils.getLianZhongPass());
			return getTextOfOCRResult(result);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 调用联众接口，获取验证码识别接口。<br/>
	 * <p>
	 * 接口格式：<br/>
	 * Y7SSA|!|332082732&worker
	 * </p>
	 * 
	 * @param result
	 *            ：打码返回接口
	 * @return
	 */
	private static String getTextOfOCRResult(String result) {
		return result.substring(0, result.indexOf(split));
	}

}
