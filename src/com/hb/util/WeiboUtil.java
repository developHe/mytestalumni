package com.hb.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences.Editor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;

public class WeiboUtil {

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
	public static boolean hasAuth(){
		String accessToken = ApplicationEnvironment.getInstance().getPreferences().getString(Constants.kACCESS_TOKEN, null);
		if (null == accessToken || accessToken.trim().equals("")){
			return false;
		}
		
		return true;
	}
	
	public static void setToken(String accessToken){
		Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
		editor.putString(Constants.kACCESS_TOKEN, accessToken);
		editor.commit();
	}
	
	public static String getToken(){
		return ApplicationEnvironment.getInstance().getPreferences().getString(Constants.kACCESS_TOKEN, "");
	}

	/**
	 * 格式化微博的日期
	 * 
	 * @param date
	 *            微博日期
	 * @return 格式化之后的字符串
	 */
	public static String formatWeiboDate(Date date) {
		return simpleDateFormat.format(date);
	}

	/**
	 * 格式化占用的空间大小
	 * 
	 * @param spaceUsed
	 *            使用的空间大小
	 * @return 空间大小的字符串表示
	 */
	public static String formatSpaceSize(double spaceUsed) {
		if (spaceUsed < 1) {
			return "不到 1M";
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			return decimalFormat.format(spaceUsed) + "M";
		}
	}
}
