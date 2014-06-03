package com.hb.activity.component;

import java.util.ArrayList;
import java.util.List;

import weibo4j.model.Status;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.activity.SinaOAuthActivity;
import com.hb.activity.WeiboDetailActivity;
import com.hb.activity.WeiboListActivity;
import com.hb.model.WeiboItemModel;
import com.hb.util.ListViewUtil;
import com.hb.util.WeiboUtil;
import com.hb.view.TimelineAdapter;

public class SchoolWeiboRelativeLayout extends RelativeLayout implements OnClickListener {
	
	private Button loginSinaWeiboButton = null;
	private ListView weiboListView = null;
	private Button weiboMoreButton = null;
	
	private List<Status> weiboList = null;
	private TimelineAdapter weiboAdapter = null;
	
	public SchoolWeiboRelativeLayout(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.layout_school_weibo, this, true); 
        
        this.init();
	}

	private void init(){
		loginSinaWeiboButton = (Button) this.findViewById(R.id.loginSinaWeiboBtn);
		loginSinaWeiboButton.setOnClickListener(this);
		loginSinaWeiboButton.setVisibility(WeiboUtil.hasAuth()? View.GONE : View.VISIBLE);
		
		weiboListView = (ListView) this.findViewById(R.id.lv_timeline);
		weiboListView.setOnItemClickListener(new WeiBoItemClickListener());
		
		weiboMoreButton = (Button) this.findViewById(R.id.rl_school_weibo_more);
		weiboMoreButton.setOnClickListener(new WeiboMoreListener());
		weiboMoreButton.setVisibility(WeiboUtil.hasAuth()? View.VISIBLE : View.GONE);
		
		weiboList = new ArrayList<Status>();
		weiboAdapter = new TimelineAdapter(weiboList);
		weiboListView.setAdapter(weiboAdapter);
		ListViewUtil.setListViewHeightBasedOnChildren(weiboListView);
	}
	
	public void refresh(List<Status> list){
		weiboList = new ArrayList<Status>(list);
		
		if (weiboList.size() > 0){
			this.setVisibility(View.VISIBLE);
			weiboMoreButton.setVisibility(View.VISIBLE);
			weiboAdapter.setList(weiboList);
			weiboAdapter.notifyDataSetChanged();
			ListViewUtil.setListViewHeightBasedOnChildren(weiboListView);
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.loginSinaWeiboBtn:
			Intent intent = new Intent(BaseActivity.getTopActivity(), SinaOAuthActivity.class);
			BaseActivity.getTopActivity().startActivityForResult(intent, 100);
			
			break;
		}
	}
	
	class WeiBoItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Status status = weiboList.get(arg2);
			
			Intent intent = new Intent(BaseActivity.getTopActivity(), WeiboDetailActivity.class);
			intent.putExtra("status", new WeiboItemModel(status));
			BaseActivity.getTopActivity().startActivityForResult(intent, 100);
		}
	}
	
	class WeiboMoreListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(BaseActivity.getTopActivity(), WeiboListActivity.class);
			BaseActivity.getTopActivity().startActivityForResult(intent, 100);
		}
		
	}
	
}
