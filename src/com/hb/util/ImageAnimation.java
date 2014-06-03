package com.hb.util;

import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * ͼƬ�ƶ��Ķ���Ч��
 * @Description: ͼƬ�ƶ��Ķ���Ч��

 * @File: ImageAnimatioin.java

 * @Package com.image.indicator.utility

 * @Author Hanyonglu

 * @Date 2012-6-17 ����11:57:29

 * @Version V1.0
 */
public class ImageAnimation {
	/**
	 * ����ͼ���ƶ�����Ч��
	 * @param v
	 * @param startX
	 * @param toX
	 * @param startY
	 * @param toY
	 */
	public static void SetImageSlide(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(100);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
}
