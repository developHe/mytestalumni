package com.hb.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hb.R;

/** 继承该类并实现其五个抽象方法即可 */
public abstract class AbsActivityGroup extends ActivityGroup{
	
	/** 源Intent */
	protected Intent fromIntent;
	
	/** 功能模块跳转的目标Intent */
	protected Intent targetIntent = new Intent();
	
	// 当前选中的项目的tag
	private int selectedImageWithTextViewId = 0;
	
	/** 用来加载子Activity的布局 */
	private LinearLayout container = null;
	
	/** 选项卡的所有标签 */
	private ImageButton[] imageWithTextViews = null;
	
	/** 选项卡所有标签的ID */
	private int[] imageWithTextViewIds = null;
	
	/** 选项卡所有标签的图标ID */
	private int[] imageWithTextViewImageIds;
	
	/** 标签ID对应的初始Activity集合 */
	private Map<Integer,Class<? extends Activity>> classes = new HashMap<Integer,Class<? extends Activity>>();
	
	/**
	 * 在子类中实现的设定布局的方法，Activity的布局的id必须为activity_group_container；
	 * 选项卡的id必须为activity_group_radioGroup
	 */
	protected abstract int getLayoutResourceId();
	
	/** 在子类中需要实现的获取选项卡所有标签的ID的方法 */ 
	protected abstract int[] getImageWithTextViewIds();
	
	/** 在子类中需要实现的获取选项卡所有标签的图标的方法 */ 
	protected abstract int[] getImageWithTextViewImageIds();
	
	/** 在子类中需要实现的获取选项卡所有标签ID对应的初始Activity的方法 */ 
	public abstract Class<? extends Activity>[] getClasses();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());         
        // 获取源Intent
		fromIntent = getIntent();
		// 设定原始数据
        setData();
        // 初始化控件
        initWidgetStatic();
    }
			
	/** 设定数据源的方法 */ 
	protected  void setData(){
		imageWithTextViewIds = getImageWithTextViewIds();
		imageWithTextViewImageIds = getImageWithTextViewImageIds();
		
		for(int i=0;i<imageWithTextViewIds.length;i++){
			classes.put(imageWithTextViewIds[i], getClasses()[i]);
		}
		
		this.selectedImageWithTextViewId = this.imageWithTextViewIds[fromIntent.getIntExtra("TAG", 0)];
	}
	
	/** 初始化控件 */ 
	protected void initWidgetStatic(){
		container = (LinearLayout) findViewById(R.id.activity_group_container);
		imageWithTextViews = new ImageButton[imageWithTextViewIds.length];
		for(int i=0;i<imageWithTextViews.length;i++){
			imageWithTextViews[i] = (ImageButton) findViewById(imageWithTextViewIds[i]);
			if(imageWithTextViewImageIds != null){
				imageWithTextViews[i].setImageResource(imageWithTextViewImageIds[i]);
				
			}
			
			imageWithTextViews[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					selectedImageWithTextViewId = view.getId();
					for (int i=0; i<imageWithTextViewIds.length; i++){
						if (selectedImageWithTextViewId == imageWithTextViewIds[i]){
							//imageWithTextViews[i].setBackgroundResource(R.drawable.img_group_selected);
							imageWithTextViews[i].setSelected(true);
							
						} else {
							//imageWithTextViews[i].setBackgroundResource(0);
							imageWithTextViews[i].setSelected(false);
						}
					}
					
					targetIntent.setClass(AbsActivityGroup.this, classes.get(selectedImageWithTextViewId));
					//launchActivity(targetIntent);
					launchNewActivity(targetIntent);
				}
			});
		}

		//((ImageButton) findViewById(selectedImageWithTextViewId)).setBackgroundResource(R.drawable.img_group_selected);
		((ImageButton) findViewById(selectedImageWithTextViewId)).setSelected(true);
		
		// 初始化加载
		targetIntent.setClass(AbsActivityGroup.this, classes.get(this.selectedImageWithTextViewId));
		launchNewActivity(targetIntent);
	}
	
	/** ActivityGroup加载新的子Activity的方法(创建新的) */ 
	public void launchNewActivity(Intent intent) {
		container.removeAllViews();
		View view = getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName() + this.selectedImageWithTextViewId,
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView();
		
		/*
		Animation fadeInAnimation = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);
		view.startAnimation(fadeInAnimation);
		*/
		
		container.addView(view);
	}
	
	/** ActivityGroup加载新的子Activity的方法(创建新的) */ 
	public void launchNewActivityForResult(AbsSubActivity requestSubActivity,
			Intent intent, int requestCode) {
		intent.putExtra("requestCode", requestCode);
		container.removeAllViews();
		View view = getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName() + this.selectedImageWithTextViewId, 
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		/*
		Animation fadeInAnimation = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);
		view.startAnimation(fadeInAnimation);
		**/
		container.addView(view);
		
		((AbsSubActivity)getCurrentActivity()).setRequestSubActivity(requestSubActivity);
	}
	
	/** ActivityGroup加载子Activity的方法(先看有没有，有则加载原来的，否则创建新的) */ 
	public void launchActivity(Intent intent) {
		container.removeAllViews();
		View view = getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName() + this.selectedImageWithTextViewId, 
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView();
		
		/*
		Animation fadeOutAnimation = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out);
		view.startAnimation(fadeOutAnimation);
		
		 **/
		container.addView(view);
	}
	
}
