package com.hb.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	
	public static String getCurrentYear(){
		return new SimpleDateFormat("yyyy").format(new Date());
	}
	public static String getCurrentMonth(){
		return new SimpleDateFormat("MM").format(new Date());
	}
	public static String getCurrentDd(){
		return new SimpleDateFormat("dd").format(new Date());
	}
	
	public static String getCurrentYearMonthDay(){
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
}
