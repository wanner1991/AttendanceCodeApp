package com.mm.attendancecodeapp.activity;

import android.content.Context;

import com.miebo.utils.SPUtil;

public class AppConstant {

	/**
	 * ��ȡ�����Url��Ŀ¼
	 * 
	 * @param context
	 * @return
	 */
	public static String getRootUrl(Context context) {
		return "http://" + SPUtil.get(context, "IP", "") + "/Handler1.ashx?";
	}

	/**
	 * ��ȡ�����Url servlet Ŀ¼
	 * 
	 * @param context
	 * @return
	 */
	public static String getUrl(Context context) {
		return getRootUrl(context);
	}

}
