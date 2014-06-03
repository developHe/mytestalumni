package com.hb.network;

import android.app.Application;
import com.hb.activity.BaseActivity;
import com.hb.client.ApplicationEnvironment;
import com.loopj.android.http.AsyncHttpClient;
import java.util.ArrayList;
import java.util.Iterator;

public class LKHttpRequestQueue {
	public static ArrayList<LKHttpRequestQueue> queueList = new ArrayList<LKHttpRequestQueue>();
	private int completedTag = 0;
	private int finishedTag = 0;
	private LKHttpRequestQueueDone queueDone;
	private ArrayList<LKHttpRequest> requestList = null;

	public LKHttpRequestQueue() {
		requestList = new ArrayList<LKHttpRequest>();
		queueList.add(this);
	}

	public LKHttpRequestQueue addHttpRequest(LKHttpRequest...httpRequests) {
		int i = 0;
		while (true) {
			if (i >= httpRequests.length)
				return this;
			LKHttpRequest localLKHttpRequest = httpRequests[i];
			double d = this.requestList.size();
			int k = (int) Math.pow(2.0D, d);
			localLKHttpRequest.setTag(k);
			localLKHttpRequest.setRequestQueue(this);
			requestList.add(localLKHttpRequest);
			i += 1;
		}
	}

	public void cancel() {
		Iterator localIterator = this.requestList.iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				this.requestList.clear();
				return;
			}
			AsyncHttpClient localAsyncHttpClient = ((LKHttpRequest) localIterator
					.next()).getClient();
			Application localApplication = ApplicationEnvironment.getInstance()
					.getApplication();
			// localAsyncHttpClient.cancelRequests(localApplication, 1);
			localAsyncHttpClient.cancelRequests(localApplication, true);
		}
	}

	public void executeQueue(String prompt, LKHttpRequestQueueDone queueDone) {
		if (!ApplicationEnvironment.getInstance().checkNetworkAvailable())
			BaseActivity.getTopActivity().showToast("网络连接不可用，请稍候重试");
		if (prompt != null) {
			try {
				BaseActivity.getTopActivity().showDialog(0x2, prompt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.queueDone = queueDone;
		this.queueDone.setRequestQueue(this);
		this.completedTag = 0;
		this.finishedTag = 0;

		Iterator localIterator = this.requestList.iterator();
		while (localIterator.hasNext())
			((LKHttpRequest) localIterator.next()).send();
	}

	public void updataFinishedTag(int tag) {

		try {
			int i = this.finishedTag + tag;
			this.finishedTag = i;
			int j = this.finishedTag;
			double d = this.requestList.size();
			int k = (int) Math.pow(2.0D, d) + -1;
			if (j == k) {
				this.requestList.clear();
				this.queueDone.onFinish();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateCompletedTag(int paramInt) {
		try {
			int i = this.completedTag + paramInt;
			this.completedTag = i;
			int j = this.completedTag;
			double d = this.requestList.size();
			int k = (int) Math.pow(2.0D, d) + -1;
			if (j == k)
				queueDone.onComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
